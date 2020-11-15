package com.NoSQl;

public class OwnerSearchObjectBuilder<IDType> {
    private OwnerSearchObject<IDType> searchObject = new OwnerSearchObject<>();

    public OwnerSearchObjectBuilder<IDType> withFirstName (String firstName) {
        searchObject.firstName = firstName;
        return this;
    }

    public OwnerSearchObjectBuilder<IDType> withLastName (String lastName) {
        searchObject.lastName = lastName;
        return this;
    }

    public OwnerSearchObjectBuilder<IDType> withPhoneNumber (String phoneNumber) {
        searchObject.phoneNumber = phoneNumber;
        return this;
    }

    public OwnerSearchObjectBuilder<IDType> withEmail (String email) {
        searchObject.email = email;
        return this;
    }

    public OwnerSearchObjectBuilder<IDType> withOwnerId (IDType ownerId) {
        searchObject.ownerId = ownerId;
        return this;
    }

    public OwnerSearchObjectBuilder<IDType> withFkCityId (IDType fkCityId) {
        searchObject.fkCityId = fkCityId;
        return this;
    }

    public OwnerSearchObject<IDType> build () {
        OwnerSearchObject<IDType> temp = searchObject;
        searchObject = new OwnerSearchObject<IDType>();
        return temp;
    }

}
