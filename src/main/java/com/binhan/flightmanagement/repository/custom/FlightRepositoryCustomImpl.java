package com.binhan.flightmanagement.repository.custom;

import com.binhan.flightmanagement.dto.FilterFlightDto;
import com.binhan.flightmanagement.models.FlightEntity;
import com.binhan.flightmanagement.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


import java.lang.reflect.Field;
import java.util.List;

public class FlightRepositoryCustomImpl implements FlightRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<FlightEntity> filterFlights(FilterFlightDto filterFlightDto) {
        String sql = createQuery(filterFlightDto);
        Query query = entityManager.createNativeQuery(sql,FlightEntity.class);
        return query.getResultList();
    }

    private String createQuery(FilterFlightDto filterFlightDto) {
        StringBuilder finalQuery = new StringBuilder("select f.* from flights f");
        StringBuilder whereQuery = new StringBuilder();
        StringBuilder joinQuery = new StringBuilder();
        buildQuery(filterFlightDto, whereQuery, joinQuery);
        finalQuery.append(joinQuery).append(" WHERE 1 = 1 ").append(whereQuery)
                .append(" group by f.id")
        ;
        return finalQuery.toString();
    }

    private void buildQuery(FilterFlightDto filterFlightDto, StringBuilder whereQuery, StringBuilder joinQuery) {
        int temp = 0;
        //StringBuilder sql= new StringBuilder();
        try {
            Field[] fields = FilterFlightDto.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object objectValue = field.get(filterFlightDto);
                if (!fieldName.equals("departureCountry") && !fieldName.equals("arrivalCountry")
                        && !fieldName.equals("arrivalAirport")
                        && !fieldName.equals("departureAirport")) {
                    if (!StringUtils.isNullOrEmpty(objectValue)) {
                        if (field.getType().getName().equals("java.lang.String")) {
                            if(field.getName().equals("arrivalTime")){
                                whereQuery.append(" and TO_CHAR(f.arrival_time, 'DD-MM-YYYY') like '%" + objectValue + "%' ");
                            }else{
                                whereQuery.append(" and TO_CHAR(f.departure_time, 'DD-MM-YYYY') like '%" + objectValue + "%' ");
                            }
                        }
                    }
                }
                if (fieldName.contains("Airport")) {
                    if (!StringUtils.isNullOrEmpty(objectValue)) {
                        joinQuery.append(
                                "\n" + "inner join airports a1 ON f.departure_airport_id = a1.id \n" + "\n");
                        joinQuery.append(
                                "\n" + "inner join airports a2 ON f.arrival_airport_id = a2.id \n" + "\n");
                        if (fieldName.equals("departureAirport")) {
                            whereQuery.append(" and f.departure_airport_id IN (SELECT id FROM airports WHERE airport_name LIKE '%" + objectValue + "%') ");
                        }
                        if (fieldName.equals("arrivalAirport")) {
                            whereQuery.append(" and f.arrival_airport_id IN (SELECT id FROM airports WHERE airport_name LIKE '%" + objectValue + "%') ");
                        }
                    }
                }


                if (fieldName.contains("Country")) {
                    if (!StringUtils.isNullOrEmpty(objectValue)) {
                        if (fieldName.equals("departureCountry")) {
                            joinQuery.append(
                                    "\n" + "inner join countries c1 ON c1.id=a1.country_id \n" + "\n");
                            whereQuery.append(" and c1.country_name LIKE '%" + objectValue + "%' ");
                        }
                        if (fieldName.equals("arrivalCountry")) {
                            joinQuery.append(
                                    "\n" + "inner join countries c2 ON c2.id=a2.country_id \n" + "\n");
                            whereQuery.append(" and c2.country_name LIKE '%" + objectValue + "%' ");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
