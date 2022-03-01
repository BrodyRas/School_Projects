package Results;

import Models.Person;

import java.util.Arrays;
import java.util.Objects;

public class PersonResult {
    public PersonResult(Person[] persons) {
        this.persons = persons;
        this.person = null;
        this.message = null;
    }

    public PersonResult(Person person) {
        this.persons = null;
        this.person = person;
        this.message = null;
    }

    public PersonResult(String message) {
        this.persons = null;
        this.person = null;
        this.message = message;
    }

    private Person[] persons;
    private Person person;
    private String message;

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonResult)) return false;
        PersonResult that = (PersonResult) o;
        return Arrays.equals(getPersons(), that.getPersons()) &&
                Objects.equals(getPerson(), that.getPerson()) &&
                Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getPerson(), getMessage());
        result = 31 * result + Arrays.hashCode(getPersons());
        return result;
    }
}
