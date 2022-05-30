package com.study.jwt.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY : mysql 사용 시 auto increment
    private long id;
    private String username;
    private String password;
    private String roles; // USER, ADMIN ...

    // Role이 하나면 이걸 만들 필요가 없음
    public List<String> getRoleList() {
        if(this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }

        // null 방지 : 빈 객체 리턴
        return new ArrayList<>();
    }
}
