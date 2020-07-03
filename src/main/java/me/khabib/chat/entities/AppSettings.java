package me.khabib.chat.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("app")
@Data
public class AppSettings extends Settings{
    protected String os;

    protected long memory;
}
