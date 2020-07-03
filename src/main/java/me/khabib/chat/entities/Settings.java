package me.khabib.chat.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn (name = "type", discriminatorType = DiscriminatorType.STRING)
public class Settings {
    @Id
    @GeneratedValue(generator = "uuidgen")
    @GenericGenerator(name = "uuidgen", strategy = "uuid")
    protected String id;

    @Column(length = 15, nullable = false)
    protected String ip;

    @Column(name = "is_active")
    protected Boolean isActive;

}
