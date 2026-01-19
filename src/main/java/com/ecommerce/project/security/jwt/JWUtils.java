package com.ecommerce.project.security.jwt;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

@Component
public class JWUtils {

    private int jwtExpirationMs=350000;
    // private String jwtSecret="bXlTdXBlclNlY3JldEp3dEtleUZvckhTMjU2ISE=";
//    private static final String jwtSecret =
//            "bXlTdXBlclNlY3JldEp3dEtleUZvckhTMjU2QWxnb3JpdGhtISE=";
    private static final String jwtSecret =
            "YS1zdHJpbmctc2VjcmV0LWF0LWxlYXN0LTI1Ni1iaXRzLWxvbmc=";


    private  static final Logger logger= LoggerFactory.getLogger(JWUtils.class);
    public String getJwtFromheader(HttpServletRequest request){

        String bearerToken=request.getHeader("Authorization");

        logger.debug("Autherization Header :{}",bearerToken);

        if(bearerToken !=null && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }else{
            return null;
        }
    }


    public String generateFromUserName(UserDetails userDetails){

        String  userName=userDetails.getUsername();
        return Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime()+jwtExpirationMs)))
                .signWith(key())
                .compact();
    }

    String getUserNameFromJwt(String token){

        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }


//    private Key key(){
//        //return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }


    Boolean validateToken(String authJwt){

        try {
            System.out.println("validate");
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authJwt);
            return true;
        }catch (MalformedJwtException e){
            System.out.println("invalid jwt tokn :{}" +e.getMessage());
        }catch (ExpiredJwtException e){
            System.out.println("jwt is ecpire : {}" +e.getMessage());
        }catch (UnsupportedJwtException e){
            System.out.println("upsopered the jswt :{}" +e.getMessage());
        }catch (IllegalArgumentException e){
            System.out.println("illgale argumgen exceptin" +e.getMessage());
        }
        return false;

    }
}

