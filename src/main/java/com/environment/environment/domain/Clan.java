package com.environment.environment.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Clan.
 */
@Entity
@Table(name = "CLAN")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Clan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "CLAN_MEMBERS",
               joinColumns = @JoinColumn(name="Clans_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="memberss_id", referencedColumnName="ID"))
    private Set<User> memberss = new HashSet<>();

    @ManyToOne
    private User chieftain;
    
    @ManyToOne
    private Battleground battleground;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getMemberss() {
        return memberss;
    }

    public void setMemberss(Set<User> users) {
        this.memberss = users;
    }

    public User getChieftain() {
        return chieftain;
    }

    public void setChieftain(User user) {
        this.chieftain = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Clan clan = (Clan) o;

        if ( ! Objects.equals(id, clan.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Clan{" +
                "id=" + id +
                ", name='" + name + "'" +
                '}';
    }
}
