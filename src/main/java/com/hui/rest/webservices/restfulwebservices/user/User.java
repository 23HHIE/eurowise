package com.hui.rest.webservices.restfulwebservices.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hui.rest.webservices.restfulwebservices.expense.ExpenditureList;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


//@JsonIgnoreProperties({"password", "role"}) //在将Java对象序列化为JSON时，忽略指定的属性，隐藏敏感信息
@Entity(name = "user_details")
@Data
//@JsonFilter("userFilter") -->为什么加上这个注释，controller就不能正常处理请求？？？
public class User implements UserDetails {

    public User() {
    }

    @Id //实体类中的主键注释
    @GeneratedValue  //主键生成方式是自动生成
    private Long id;
    @Size(min = 2, message = "Name should have at least 2 characters")
    private String name;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    //@NotBlank(message = "Password can not be blank") -->不清楚为什么加上这个注释就会无法post方法
    //@JsonIgnore
    private String password;
    private String email;
    //@JsonIgnore
    private String role="USER";


    private int status;

    @Getter
    @Transient
    private String token;

    /**
     * 在 User 实体和 Expense 实体之间建立了一对多的关系，同时告诉 JPA 框架，
     * 在 Expense 实体中通过属性 "user" 来维护这个关系。
     */
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ExpenditureList> expense;

    public User(Long id, String name, String password, String email, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setExpense(List<ExpenditureList> expenseLists) {
        this.expense = expenseLists;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == 0;
    }
}
