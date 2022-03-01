package Models;

import java.util.Objects;

public class Person {
    public Person(String personID,
                  String descendant,
                  String firstName,
                  String lastName,
                  String gender) {
        this.personID = personID;
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = null;
        this.mother = null;
        this.spouse = null;
    }

    public Person(String personID,
                  String descendant,
                  String firstName,
                  String lastName,
                  String gender,
                  String father,
                  String mother,
                  String spouse) {
        this.personID = personID;
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    private String personID, descendant, firstName, lastName, gender, father, mother, spouse;

    /**
     * Sets the spouse of person, after assuring the genders are correct
     *
     * @param spouse spouse to be added
     * @throws GenderException thrown if spouses' genders match
     */
    public void setSpouse(Person spouse) throws GenderException {
        if (spouse.getGender() == this.gender) {
            throw new GenderException("Spouse genders cannot be the same! " +
                    "ACTUAL: " + firstName + " " + lastName + "(" + gender + "), " +
                    spouse.getFirstName() + " " + spouse.getLastName() + "(" + spouse.getGender() + ")");
        } else {
            this.spouse = spouse.getPersonID();
        }
    }

    /**
     * Sets the parents of person, after assuring the genders are correct
     *
     * @param father father to be added
     * @param mother mother to be added
     * @throws GenderException thrown if order of parent genders is incorrect
     */
    public void setParents(Person father, Person mother) throws GenderException {
        if (father.getGender() != "m" || mother.getGender() != "f") {
            throw new GenderException("Incorrect order of parent genders! USAGE: father mother | ACTUAL: " +
                    father.getFirstName() + " " + father.getLastName() + "(" + father.getGender() + "), " +
                    mother.getFirstName() + " " + mother.getLastName() + "(" + mother.getGender() + ")");
        } else {
            this.father = father.getPersonID();
            this.mother = mother.getPersonID();
        }
    }

    public String getPersonID() {
        return personID;
    }

    public String getDescendant() {
        return descendant;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFather() {
        return father;
    }

    public String getMother() {
        return mother;
    }

    public String getSpouse() {
        return spouse;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(personID, person.getPersonID()) &&
                Objects.equals(descendant, person.getDescendant()) &&
                Objects.equals(firstName, person.getFirstName()) &&
                Objects.equals(lastName, person.getLastName()) &&
                Objects.equals(gender, person.getGender()) &&
                Objects.equals(father, person.getFather()) &&
                Objects.equals(mother, person.getMother()) &&
                Objects.equals(spouse, person.getSpouse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPersonID(), getDescendant(), getFirstName(), getLastName(), getGender(), getFather(), getMother(), getSpouse());
    }
}
