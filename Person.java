import javafx.beans.property.*;

public class Person {
	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleStringProperty number;

	public  Person() {}

	public Person(String firstName, String lastName, String number) {
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.number = new SimpleStringProperty(number);
	}

	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}
	public String getFirstName() {
		return this.firstName.get();
	}
	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}
	public String getLastName() {
		return this.lastName.get();
	}
	public void setNumber(String number) {
		this.number.set(number);
	}
	public String getNumber() {
		return this.number.get();
	}
	@Override
	public String toString() {
		return firstName + " " + lastName + " " + number;
	}
}