package com.webapp.bankingportal.mapper;

import com.webapp.bankingportal.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-01T05:47:12+0530",
    comments = "version: 1.6.0.Beta2, compiler: Eclipse JDT (IDE) 3.40.0.z20241112-1021, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public void updateUser(User source, User target) {
        if ( source == null ) {
            return;
        }

        if ( source.getAccount() != null ) {
            target.setAccount( source.getAccount() );
        }
        if ( source.getAddress() != null ) {
            target.setAddress( source.getAddress() );
        }
        if ( source.getCountryCode() != null ) {
            target.setCountryCode( source.getCountryCode() );
        }
        if ( source.getEmail() != null ) {
            target.setEmail( source.getEmail() );
        }
        if ( source.getId() != null ) {
            target.setId( source.getId() );
        }
        if ( source.getName() != null ) {
            target.setName( source.getName() );
        }
        if ( source.getPassword() != null ) {
            target.setPassword( source.getPassword() );
        }
        if ( source.getPhoneNumber() != null ) {
            target.setPhoneNumber( source.getPhoneNumber() );
        }
    }
}
