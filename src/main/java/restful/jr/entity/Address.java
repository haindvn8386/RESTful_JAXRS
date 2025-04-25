package restful.jr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Address")
public class Address extends BaseEntity<Long>   {

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "ward")
    private String ward;

    @Column(name = "district")
    private String district;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "address_type")
    private Integer addressType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
