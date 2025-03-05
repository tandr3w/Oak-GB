public class StudentInfo {
	public int studentNumber;
	public String firstName;
	public String lastName;
	public StudentInfo left;
	public StudentInfo right;
	
	public StudentInfo(int sN, String fN, String lN) {
		studentNumber = sN;
		firstName = fN;
		lastName = lN;
		left = null;
		right = null;
	}

}
