package com.TtPP.builders;

import com.TtPP.entities.Owner;

public class OwnerBuilder {
    private Owner owner = new Owner(
            -1,
            "",
            "",
            "",
            "",
            -1
    );

    public OwnerBuilder withOwnerId (int id) {
        this.owner.setOwnerId(id);
        return this;
    }

    public OwnerBuilder withFkCityId (int id) {
        this.owner.setFkCityId(id);
        return this;
    }

    public OwnerBuilder withFirstName (String firstName) {
        this.owner.setFirstName(firstName);
        return this;
    }

    public OwnerBuilder withLastName (String lastName) {
        this.owner.setLastName(lastName);
        return this;
    }

    public OwnerBuilder withEmail (String email) {
        this.owner.setEmail(email);
        return this;
    }

    public OwnerBuilder withPhoneNumber (String phoneNumber) {
        this.owner.setPhoneNumber(phoneNumber);
        return this;
    }

    public Owner build () {
        Owner tempOwner = this.owner;
        this.owner = new Owner(
                -1,
                "",
                "",
                "",
                "",
                -1
        );
        return tempOwner;
    }
}
