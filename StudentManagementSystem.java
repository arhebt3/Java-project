package bootcamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger; // For generating unique IDs


abstract class User {
    private String username;
    private String password;
    private String name;


    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

  
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public String getName() {
        return name;
    }


    public abstract void showMenu(Scanner scanner, StudentManagementSystem system);
}

class Admin extends User {
  
    public Admin(String username, String password) {
        super(username, password, "Admin"); // Admin's name is set to "Admin" by default
    }

 
    @Override
    public void showMenu(Scanner scanner, StudentManagementSystem system) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Course");
            System.out.println("2. Manage Course (Add/Remove Subjects)");
            System.out.println("3. View All Registered Students");
            System.out.println("4. View Student Exam Results");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number (1-5).");
                scanner.nextLine(); // Consume the invalid input to prevent infinite loop
                continue; // Continue to the next iteration of the loop for valid input
            }

            switch (choice) {
                case 1:
                    system.addCourse(scanner);
                    break;
                case 2:
                    system.manageCourseSubjects(scanner);
                    break;
                case 3:
                    system.viewAllRegisteredStudents();
                    break;
                case 4:
                    system.viewStudentExamResults(scanner);
                    break;
                case 5:
                    System.out.println("Admin logged out successfully.");
                    return; // Exit admin menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}


class Student extends User {
    private String studentId;
    private String email;
    private int age;
    private List<Course> enrolledCourses;
    private List<Subject> enrolledSubjects;
    private Map<String, Integer> examScores; // Stores exam scores: key = subjectId, value = score

    // Static counter to generate unique student IDs
    private static final AtomicInteger idCounter = new AtomicInteger(1000);

   
    public Student(String username, String password, String name, int age, String email) {
        super(username, password, name);
        this.studentId = "STU" + idCounter.getAndIncrement(); // Generate a unique ID
        this.age = age;
        this.email = email;
        this.enrolledCourses = new ArrayList<>();
        this.enrolledSubjects = new ArrayList<>();
        this.examScores = new HashMap<>();
    }

    
    public String getStudentId() {
        return studentId;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public List<Subject> getEnrolledSubjects() {
        return enrolledSubjects;
    }

    public Map<String, Integer> getExamScores() {
        return examScores;
    }

  
    public void enrollCourse(Course course) {
        if (!enrolledCourses.contains(course)) { // Check using Course's equals method
            enrolledCourses.add(course);
            System.out.println(getName() + " successfully enrolled in " + course.getCourseName() + ".");
        } else {
            System.out.println(getName() + " is already enrolled in " + course.getCourseName() + ".");
        }
    }

    
    public void enrollSubject(Subject subject) {
        if (!enrolledSubjects.contains(subject)) { // Check using Subject's equals method
            enrolledSubjects.add(subject);
            System.out.println(getName() + " successfully enrolled in subject " + subject.getSubjectName() + ".");
        } else {
            System.out.println(getName() + " is already enrolled in subject " + subject.getSubjectName() + ".");
        }
    }

   
    public void setExamScore(String subjectId, int score) {
        examScores.put(subjectId, score);
    }

    
    @Override
    public void showMenu(Scanner scanner, StudentManagementSystem system) {
        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("Hello, " + getName() + " (ID: " + getStudentId() + ")!");
            System.out.println("1. View Available Courses");
            System.out.println("2. Select Course and Choose Subjects");
            System.out.println("3. Take Exam");
            System.out.println("4. View My Exam Result");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number (1-5).");
                scanner.nextLine(); // Consume the invalid input
                continue; // Continue to the next iteration of the loop
            }

            switch (choice) {
                case 1:
                    system.viewAvailableCourses();
                    break;
                case 2:
                    system.studentSelectCourseAndSubjects(scanner, this); // Pass current student instance
                    break;
                case 3:
                    system.takeExam(scanner, this); // Pass current student instance
                    break;
                case 4:
                    system.viewMyExamResult(this); // Pass current student instance
                    break;
                case 5:
                    System.out.println("Student logged out successfully.");
                    return; // Exit student menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    
    @Override
    public String toString() {
        return "ID: " + studentId + ", Name: " + getName() + ", Age: " + age + ", Email: " + email + ", Username: " + getUsername();
    }
}


class Course {
    private String courseId;
    private String courseName;
    private List<Subject> subjects;

    // Static counter to generate unique course IDs
    private static final AtomicInteger idCounter = new AtomicInteger(100);

    /**
     * Constructor for the Course class.
     * @param courseName The name of the course.
     */
    public Course(String courseName) {
        this.courseId = "COU" + idCounter.getAndIncrement();
        this.courseName = courseName;
        this.subjects = new ArrayList<>();
    }

    // Getter methods for course properties
    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

   
    public void addSubject(Subject subject) {
        if (!subjects.contains(subject)) { // Uses Subject's equals method
            subjects.add(subject);
            System.out.println("Subject '" + subject.getSubjectName() + "' (ID: " + subject.getSubjectId() + ") added to course '" + courseName + "'.");
        } else {
            System.out.println("Subject '" + subject.getSubjectName() + "' (ID: " + subject.getSubjectId() + ") already exists in course '" + courseName + "'.");
        }
    }

   
    public boolean removeSubject(String subjectId) {
        Subject subjectToRemove = null;
        for (Subject s : subjects) {
            if (s.getSubjectId().equals(subjectId)) {
                subjectToRemove = s;
                break;
            }
        }
        if (subjectToRemove != null) {
            subjects.remove(subjectToRemove);
            System.out.println("Subject '" + subjectToRemove.getSubjectName() + "' removed from course '" + courseName + "'.");
            return true;
        } else {
            System.out.println("Subject with ID '" + subjectId + "' not found in course '" + courseName + "'.");
            return false;
        }
    }

    
    @Override
    public String toString() {
        return "ID: " + courseId + ", Name: " + courseName + ", Subjects: " + subjects.size();
    }

   
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return courseId.equals(course.courseId);
    }

    
    @Override
    public int hashCode() {
        return courseId.hashCode();
    }
}

class Subject {
    private String subjectId;
    private String subjectName;

    // Static counter to generate unique subject IDs
    private static final AtomicInteger idCounter = new AtomicInteger(10000);

    
    public Subject(String subjectName) {
        this.subjectId = "SUB" + idCounter.getAndIncrement();
        this.subjectName = subjectName;
    }

    // Getter methods for subject properties
    public String getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

  
    @Override
    public String toString() {
        return "ID: " + subjectId + ", Name: " + subjectName;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return subjectId.equals(subject.subjectId); // Subjects are equal if their IDs are the same
    }

    
    @Override
    public int hashCode() {
        return subjectId.hashCode();
    }
}


class Question {
    private String questionText;
    private List<String> options;
    private int correctOptionIndex; // 0-indexed, corresponding to the options list

   
    public Question(String questionText, List<String> options, int correctOptionIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    // Getter methods for question properties
    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    /**
     * Displays the question text and its options to the console.
     */
    public void displayQuestion() {
        System.out.println(questionText);
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i)); // Display options as 1-indexed for user
        }
    }
}

/**
 * The Exam class represents an exam for a specific subject, composed of a list of questions.
 */
class Exam {
    private String examId;
    private String subjectId;
    private String subjectName; // Stored for convenience in display
    private List<Question> questions;

    // Static counter to generate unique exam IDs
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    
    public Exam(String subjectId, String subjectName, List<Question> questions) {
        this.examId = "EXAM" + idCounter.getAndIncrement();
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.questions = questions;
    }

    // Getter methods for exam properties
    public String getExamId() {
        return examId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}


public class StudentManagementSystem {
    private List<Course> courses; // Stores all available courses
    private List<Student> students; // Stores all registered students
    private List<User> allUsers; // Stores all registered users (Admins and Students)
    private List<Exam> exams; // Stores all available exams

    private User loggedInUser; // Tracks the currently logged-in user

    
    public StudentManagementSystem() {
        courses = new ArrayList<>();
        students = new ArrayList<>();
        allUsers = new ArrayList<>();
        exams = new ArrayList<>();

        // --- Initialize Dummy Data ---
        // 1. Default Admin User
        Admin defaultAdmin = new Admin("admin", "admin123");
        allUsers.add(defaultAdmin);
        System.out.println("System Initialized: Default Admin (username: admin, password: admin123) created.");

        // 2. Dummy Courses and Subjects
        Course javaCourse = new Course("Java Programming");
        Subject coreJava = new Subject("Core Java");
        Subject advancedJava = new Subject("Advanced Java");
        javaCourse.addSubject(coreJava);
        javaCourse.addSubject(advancedJava);
        courses.add(javaCourse);

        Course pythonCourse = new Course("Python for Data Science");
        Subject pythonBasics = new Subject("Python Basics");
        Subject dataAnalysis = new Subject("Data Analysis with Python");
        pythonCourse.addSubject(pythonBasics);
        pythonCourse.addSubject(dataAnalysis);
        courses.add(pythonCourse);

        Course webDevCourse = new Course("Web Development");
        Subject htmlCss = new Subject("HTML/CSS Fundamentals");
        Subject javascript = new Subject("JavaScript Essentials");
        webDevCourse.addSubject(htmlCss);
        webDevCourse.addSubject(javascript);
        courses.add(webDevCourse);

        // 3. Dummy Students
        Student student1 = new Student("alice", "pass123", "Alice Smith", 20, "alice@example.com");
        students.add(student1);
        allUsers.add(student1); // Add to allUsers for login
        student1.enrollCourse(javaCourse);
        student1.enrollSubject(coreJava);
        student1.enrollSubject(advancedJava);

        Student student2 = new Student("bob", "pass456", "Bob Johnson", 22, "bob@example.com");
        students.add(student2);
        allUsers.add(student2); // Add to allUsers for login
        student2.enrollCourse(pythonCourse);
        student2.enrollSubject(pythonBasics);
        student2.enrollSubject(dataAnalysis);

        Student student3 = new Student("charlie", "cpass", "Charlie Brown", 19, "charlie@example.com");
        students.add(student3);
        allUsers.add(student3); // Add to allUsers for login
        student3.enrollCourse(webDevCourse);
        student3.enrollSubject(htmlCss);

        // 4. Dummy Exams (minimum 5 MCQs per exam)
        // Exam for Core Java (5 questions)
        List<Question> coreJavaQuestions = new ArrayList<>();
        coreJavaQuestions.add(new Question("What is the main purpose of encapsulation in OOP?", List.of("To hide implementation details", "To allow multiple inheritance", "To enable polymorphism", "To define interfaces"), 0));
        coreJavaQuestions.add(new Question("Which keyword is used to prevent a class from being inherited?", List.of("static", "final", "abstract", "private"), 1));
        coreJavaQuestions.add(new Question("What is the default value of an instance variable of type 'int' in Java?", List.of("null", "0", "false", "undefined"), 1));
        coreJavaQuestions.add(new Question("Which of these is a checked exception in Java?", List.of("NullPointerException", "ArrayIndexOutOfBoundsException", "IOException", "ArithmeticException"), 2));
        coreJavaQuestions.add(new Question("Which Java concept allows a class to take on multiple forms?", List.of("Inheritance", "Abstraction", "Polymorphism", "Encapsulation"), 2));
        exams.add(new Exam(coreJava.getSubjectId(), coreJava.getSubjectName(), coreJavaQuestions));

        // Exam for Python Basics (5 questions)
        List<Question> pythonQuestions = new ArrayList<>();
        pythonQuestions.add(new Question("Which symbol is used for single-line comments in Python?", List.of("//", "#", "/*", "<!--"), 1));
        pythonQuestions.add(new Question("What is the output of '2 ** 3' in Python?", List.of("6", "8", "9", "23"), 1));
        pythonQuestions.add(new Question("Which function converts a string to an integer in Python?", List.of("str_to_int()", "int()", "convert_to_int()", "parse_int()"), 1));
        pythonQuestions.add(new Question("What is PEP 8?", List.of("A Python package manager", "A Python web framework", "A style guide for Python code", "A Python testing library"), 2));
        pythonQuestions.add(new Question("Which of these data types is immutable in Python?", List.of("list", "dictionary", "set", "tuple"), 3));
        exams.add(new Exam(pythonBasics.getSubjectId(), pythonBasics.getSubjectName(), pythonQuestions));

        // Exam for HTML/CSS Fundamentals (5 questions)
        List<Question> htmlCssQuestions = new ArrayList<>();
        htmlCssQuestions.add(new Question("Which HTML tag is used to define an internal style sheet?", List.of("<script>", "<css>", "<style>", "<link>"), 2));
        htmlCssQuestions.add(new Question("What does CSS stand for?", List.of("Creative Style Sheets", "Cascading Style Sheets", "Computer Style Sheets", "Colorful Style Sheets"), 1));
        htmlCssQuestions.add(new Question("Which property is used to change the background color of an element?", List.of("color", "bgcolor", "background-color", "background"), 2));
        htmlCssQuestions.add(new Question("Which HTML element is used to specify a footer for a document or section?", List.of("<bottom>", "<footer>", "<end>", "<section>"), 1));
        htmlCssQuestions.add(new Question("In CSS, how do you select an element with id 'demo'?", List.of(".demo", "#demo", "element.demo", "*demo"), 1));
        exams.add(new Exam(htmlCss.getSubjectId(), htmlCss.getSubjectName(), htmlCssQuestions));
    }

    
    public static void main(String[] args) {
        StudentManagementSystem system = new StudentManagementSystem();
        system.run();
    }

    
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Welcome to Student Management System ---");
            System.out.println("1. Login");
            System.out.println("2. Register Student");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline character
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number (1-3).");
                scanner.nextLine(); // Consume the invalid input to prevent infinite loop
                continue; // Restart the loop to ask for valid input
            }

            switch (choice) {
                case 1:
                    loginUser(scanner);
                    break;
                case 2:
                    registerNewStudent(scanner);
                    break;
                case 3:
                    System.out.println("Exiting Student Management System. Goodbye!");
                    scanner.close(); // Close the scanner before exiting the application
                    return; // Terminate the run method and thus the application
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    
    private void loginUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User foundUser = null;
        for (User user : allUsers) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                foundUser = user;
                break;
            }
        }

        if (foundUser != null) {
            loggedInUser = foundUser; // Set the current logged-in user
            System.out.println("Login successful!");
            loggedInUser.showMenu(scanner, this); // Call the appropriate menu method based on user type
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    
    private void registerNewStudent(Scanner scanner) {
        System.out.print("Enter student's full name: ");
        String name = scanner.nextLine();

        int age = -1;
        while (true) {
            System.out.print("Enter student's age: ");
            try {
                age = scanner.nextInt();
                if (age <= 0 || age > 100) { // Simple age validation
                    System.out.println("Age must be a positive number and less than 100.");
                    continue; // Ask for age again
                }
                scanner.nextLine(); // Consume newline
                break; // Valid age entered, exit loop
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numerical value for age.");
                scanner.nextLine(); // Consume the invalid input
            }
        }

        System.out.print("Enter student's email address: ");
        String email = scanner.nextLine();

        String username;
        while (true) {
            System.out.print("Choose a username: ");
            username = scanner.nextLine();
            if (isUsernameTaken(username)) {
                System.out.println("That username is already taken. Please choose a different one.");
            } else {
                break; // Unique username chosen, exit loop
            }
        }

        System.out.print("Choose a password: ");
        String password = scanner.nextLine();

        // Create a new Student object and add to lists
        Student newStudent = new Student(username, password, name, age, email);
        students.add(newStudent);
        allUsers.add(newStudent); // Also add to the list of all users for login purposes
        System.out.println("\nStudent '" + name + "' registered successfully!");
        System.out.println("Your Student ID is: " + newStudent.getStudentId());
        System.out.println("You can now login with username: " + newStudent.getUsername() + " and your chosen password.");
    }

    
    private boolean isUsernameTaken(String username) {
        for (User user : allUsers) {
            if (user.getUsername().equalsIgnoreCase(username)) { // Case-insensitive check
                return true;
            }
        }
        return false;
    }

    
    public void addCourse(Scanner scanner) {
        System.out.print("Enter the name of the new course: ");
        String courseName = scanner.nextLine();

        // Check for duplicate course names (case-insensitive)
        for (Course c : courses) {
            if (c.getCourseName().equalsIgnoreCase(courseName)) {
                System.out.println("A course with the name '" + courseName + "' already exists. Please choose a different name.");
                return;
            }
        }

        Course newCourse = new Course(courseName);
        courses.add(newCourse);
        System.out.println("Course '" + courseName + "' (ID: " + newCourse.getCourseId() + ") added successfully.");
    }

    
    public void manageCourseSubjects(Scanner scanner) {
        if (courses.isEmpty()) {
            System.out.println("No courses are available to manage subjects. Please add a course first.");
            return;
        }

        System.out.println("\n--- Available Courses for Subject Management ---");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println((i + 1) + ". " + courses.get(i).getCourseName() + " (ID: " + courses.get(i).getCourseId() + ")");
        }
        System.out.print("Enter the number of the course to manage subjects for: ");

        int courseChoice = -1;
        try {
            courseChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numerical value for course choice.");
            scanner.nextLine(); // Consume invalid input
            return; // Exit function
        }

        if (courseChoice < 1 || courseChoice > courses.size()) {
            System.out.println("Invalid course selection. Please choose a number from the list.");
            return;
        }

        Course selectedCourse = courses.get(courseChoice - 1);
        System.out.println("\n--- Managing Subjects for: " + selectedCourse.getCourseName() + " ---");

        while (true) {
            System.out.println("\nSubject Management Options:");
            System.out.println("1. Add Subject to " + selectedCourse.getCourseName());
            System.out.println("2. Remove Subject from " + selectedCourse.getCourseName());
            System.out.println("3. View All Subjects in " + selectedCourse.getCourseName());
            System.out.println("4. Back to Admin Menu");
            System.out.print("Enter your choice: ");

            int subjectActionChoice = -1;
            try {
                subjectActionChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number (1-4).");
                scanner.nextLine(); // Consume invalid input
                continue; // Loop again for valid input
            }

            switch (subjectActionChoice) {
                case 1:
                    System.out.print("Enter the name of the new subject to add: ");
                    String subjectName = scanner.nextLine();
                    // Check for duplicate subject names within THIS course
                    boolean subjectExistsInCourse = false;
                    for (Subject s : selectedCourse.getSubjects()) {
                        if (s.getSubjectName().equalsIgnoreCase(subjectName)) {
                            subjectExistsInCourse = true;
                            System.out.println("Subject '" + subjectName + "' already exists in '" + selectedCourse.getCourseName() + "'.");
                            break;
                        }
                    }
                    if (!subjectExistsInCourse) {
                        selectedCourse.addSubject(new Subject(subjectName));
                    }
                    break;
                case 2:
                    if (selectedCourse.getSubjects().isEmpty()) {
                        System.out.println("There are no subjects in '" + selectedCourse.getCourseName() + "' to remove.");
                        break;
                    }
                    System.out.println("\n--- Subjects in " + selectedCourse.getCourseName() + " ---");
                    selectedCourse.getSubjects().forEach(System.out::println); // Print all subjects
                    System.out.print("Enter the Subject ID to remove: ");
                    String subIdToRemove = scanner.nextLine();
                    selectedCourse.removeSubject(subIdToRemove);
                    break;
                case 3:
                    if (selectedCourse.getSubjects().isEmpty()) {
                        System.out.println("No subjects have been added to '" + selectedCourse.getCourseName() + "' yet.");
                    } else {
                        System.out.println("\n--- All Subjects in " + selectedCourse.getCourseName() + " ---");
                        selectedCourse.getSubjects().forEach(System.out::println);
                    }
                    break;
                case 4:
                    return; // Go back to the main Admin menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Allows an Admin to view a list of all registered students with their details.
     */
    public void viewAllRegisteredStudents() {
        if (students.isEmpty()) {
            System.out.println("No students are registered in the system yet.");
            return;
        }
        System.out.println("\n--- All Registered Students ---");
        for (Student student : students) {
            System.out.println(student); // Uses Student's toString method
            if (!student.getEnrolledCourses().isEmpty()) {
                System.out.print("  Enrolled Courses: ");
                student.getEnrolledCourses().forEach(c -> System.out.print(c.getCourseName() + " (ID: " + c.getCourseId() + "); "));
                System.out.println();
            }
            if (!student.getEnrolledSubjects().isEmpty()) {
                System.out.print("  Enrolled Subjects: ");
                student.getEnrolledSubjects().forEach(s -> System.out.print(s.getSubjectName() + " (ID: " + s.getSubjectId() + "); "));
                System.out.println();
            }
            System.out.println("--------------------------------------------------");
        }
    }

    /**
     * Allows an Admin to view exam results for a specific student or all students.
     * @param scanner The Scanner object for reading user input.
     */
    public void viewStudentExamResults(Scanner scanner) {
        if (students.isEmpty()) {
            System.out.println("No students registered to view exam results.");
            return;
        }

        System.out.println("\n--- Available Students for Result Viewing ---");
        for (Student s : students) {
            System.out.println("ID: " + s.getStudentId() + ", Name: " + s.getName());
        }

        System.out.print("Enter student ID to view results (or type 'all' to view results for all students): ");
        String studentIdInput = scanner.nextLine();

        if (studentIdInput.equalsIgnoreCase("all")) {
            for (Student student : students) {
                displayStudentResults(student);
            }
        } else {
            Student foundStudent = null;
            for (Student s : students) {
                if (s.getStudentId().equalsIgnoreCase(studentIdInput)) {
                    foundStudent = s;
                    break;
                }
            }
            if (foundStudent != null) {
                displayStudentResults(foundStudent);
            } else {
                System.out.println("Student with ID '" + studentIdInput + "' not found.");
            }
        }
    }

    
    private void displayStudentResults(Student student) {
        System.out.println("\n--- Exam Results for " + student.getName() + " (ID: " + student.getStudentId() + ") ---");
        if (student.getExamScores().isEmpty()) {
            System.out.println("No exam results available for " + student.getName() + " yet.");
            return;
        }

        for (Map.Entry<String, Integer> entry : student.getExamScores().entrySet()) {
            String subjectId = entry.getKey();
            int score = entry.getValue();
            String subjectName = getSubjectNameById(subjectId); // Get subject name using its ID

            System.out.println("Subject: " + (subjectName != null ? subjectName : "Unknown Subject") + " (ID: " + subjectId + ")");
            System.out.println("  Score: " + score + "/" + getExamQuestionsCount(subjectId)); // Display score out of total questions
            // Simple pass/fail logic (e.g., 60% of 5 questions is 3)
            if (score >= (getExamQuestionsCount(subjectId) * 0.6)) {
                System.out.println("  Status: PASS");
            } else {
                System.out.println("  Status: FAIL");
            }
        }
        System.out.println("--------------------------------------------------");
    }

    
    private String getSubjectNameById(String subjectId) {
        for (Course course : courses) {
            for (Subject subject : course.getSubjects()) {
                if (subject.getSubjectId().equals(subjectId)) {
                    return subject.getSubjectName();
                }
            }
        }
        return null;
    }

    
    private int getExamQuestionsCount(String subjectId) {
        for (Exam exam : exams) {
            if (exam.getSubjectId().equals(subjectId)) {
                return exam.getQuestions().size();
            }
        }
        return 0; // No exam found for this subject
    }


    
    public void viewAvailableCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses are available in the system at the moment.");
            return;
        }
        System.out.println("\n--- All Available Courses ---");
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            System.out.println((i + 1) + ". " + course.getCourseName() + " (ID: " + course.getCourseId() + ")");
            if (!course.getSubjects().isEmpty()) {
                System.out.println("   Subjects offered:");
                for (Subject subject : course.getSubjects()) {
                    System.out.println("     - " + subject.getSubjectName() + " (ID: " + subject.getSubjectId() + ")");
                }
            } else {
                System.out.println("   No subjects are currently available for this course.");
            }
            System.out.println("--------------------------------------------------");
        }
    }

    
    public void studentSelectCourseAndSubjects(Scanner scanner, Student currentStudent) {
        if (courses.isEmpty()) {
            System.out.println("No courses available to select. Please contact the admin to add courses.");
            return;
        }
        viewAvailableCourses(); // Show all courses for selection

        System.out.print("Enter the number of the course you wish to enroll in: ");
        int courseChoice = -1;
        try {
            courseChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numerical value for course choice.");
            scanner.nextLine(); // Consume invalid input
            return;
        }

        if (courseChoice < 1 || courseChoice > courses.size()) {
            System.out.println("Invalid course selection. Please choose a number from the list.");
            return;
        }

        Course selectedCourse = courses.get(courseChoice - 1);
        currentStudent.enrollCourse(selectedCourse); // Enroll the student in the chosen course

        if (selectedCourse.getSubjects().isEmpty()) {
            System.out.println("The selected course '" + selectedCourse.getCourseName() + "' has no subjects yet. Cannot enroll in subjects.");
            return;
        }

        while (true) {
            System.out.println("\n--- Subjects Available in " + selectedCourse.getCourseName() + " ---");
            for (int i = 0; i < selectedCourse.getSubjects().size(); i++) {
                Subject subject = selectedCourse.getSubjects().get(i);
                System.out.println((i + 1) + ". " + subject.getSubjectName() + " (ID: " + subject.getSubjectId() + ")");
            }
            System.out.println("Enter the numbers of subjects you want to enroll in (e.g., 1 3), or type '0' to finish selecting subjects: ");
            String input = scanner.nextLine();
            if (input.equals("0")) {
                System.out.println("Finished subject selection.");
                break; // Exit subject selection loop
            }

            try {
                String[] subjectChoices = input.split(" ");
                for (String sChoice : subjectChoices) {
                    int subIndex = Integer.parseInt(sChoice) - 1; // Convert to 0-indexed
                    if (subIndex >= 0 && subIndex < selectedCourse.getSubjects().size()) {
                        currentStudent.enrollSubject(selectedCourse.getSubjects().get(subIndex));
                    } else {
                        System.out.println("Warning: Invalid subject number '" + (subIndex + 1) + "' ignored.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input format. Please enter numbers separated by spaces, or '0'.");
            }
        }
    }

   
    public void takeExam(Scanner scanner, Student currentStudent) {
        // Filter subjects that the student is enrolled in AND for which an exam exists
        List<Subject> subjectsWithAvailableExams = new ArrayList<>();
        for (Subject studentEnrolledSubject : currentStudent.getEnrolledSubjects()) {
            for (Exam availableExam : exams) {
                if (availableExam.getSubjectId().equals(studentEnrolledSubject.getSubjectId())) {
                    subjectsWithAvailableExams.add(studentEnrolledSubject);
                    break; // Found an exam for this subject, move to the next enrolled subject
                }
            }
        }

        if (subjectsWithAvailableExams.isEmpty()) {
            System.out.println("You are not currently enrolled in any subjects that have an available exam.");
            System.out.println("Please enroll in subjects, or wait for an exam to be set up by the admin.");
            return;
        }

        System.out.println("\n--- Subjects You Can Take an Exam For ---");
        for (int i = 0; i < subjectsWithAvailableExams.size(); i++) {
            System.out.println((i + 1) + ". " + subjectsWithAvailableExams.get(i).getSubjectName());
        }

        System.out.print("Enter the number of the subject for which you want to take the exam: ");
        int subjectChoice = -1;
        try {
            subjectChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a numerical value for subject choice.");
            scanner.nextLine(); // Consume invalid input
            return;
        }

        if (subjectChoice < 1 || subjectChoice > subjectsWithAvailableExams.size()) {
            System.out.println("Invalid subject selection. Please choose a number from the list.");
            return;
        }

        Subject selectedSubjectForExam = subjectsWithAvailableExams.get(subjectChoice - 1);
        Exam examToTake = null;
        for (Exam exam : exams) {
            if (exam.getSubjectId().equals(selectedSubjectForExam.getSubjectId())) {
                examToTake = exam;
                break;
            }
        }

        if (examToTake == null) {
            System.out.println("Error: No exam found for '" + selectedSubjectForExam.getSubjectName() + "'. This should not happen if listed above.");
            return;
        }

        // Check if student has already taken this exam
        if (currentStudent.getExamScores().containsKey(selectedSubjectForExam.getSubjectId())) {
            System.out.println("You have already taken the exam for " + selectedSubjectForExam.getSubjectName() + ".");
            System.out.println("Your previous score: " + currentStudent.getExamScores().get(selectedSubjectForExam.getSubjectId()) + "/" + examToTake.getQuestions().size());
            System.out.print("Do you want to retake the exam? (yes/no): ");
            String retakeChoice = scanner.nextLine().trim().toLowerCase();
            if (!retakeChoice.equals("yes")) {
                System.out.println("Exam retake cancelled.");
                return;
            }
        }

        System.out.println("\n--- Starting Exam for " + selectedSubjectForExam.getSubjectName() + " ---");
        System.out.println("Total Questions: " + examToTake.getQuestions().size());
        int score = 0;

        // Iterate through questions and prompt for answers
        for (int i = 0; i < examToTake.getQuestions().size(); i++) {
            Question q = examToTake.getQuestions().get(i);
            System.out.println("\nQuestion " + (i + 1) + " of " + examToTake.getQuestions().size() + ":");
            q.displayQuestion();

            int studentAnswer = -1;
            while (true) {
                System.out.print("Enter your answer (1-" + q.getOptions().size() + "): ");
                try {
                    studentAnswer = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (studentAnswer >= 1 && studentAnswer <= q.getOptions().size()) {
                        break; // Valid answer entered
                    } else {
                        System.out.println("Invalid option. Please enter a number within the valid range.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a numerical value.");
                    scanner.nextLine(); // Consume invalid input
                }
            }

            if (studentAnswer - 1 == q.getCorrectOptionIndex()) { // Convert user's 1-indexed answer to 0-indexed
                System.out.println("Correct Answer!");
                score++;
            } else {
                System.out.println("Incorrect. The correct answer was: " + (q.getCorrectOptionIndex() + 1) + ". " + q.getOptions().get(q.getCorrectOptionIndex()));
            }
        }

        System.out.println("\n--- Exam Completed! ---");
        System.out.println("Your final score for " + selectedSubjectForExam.getSubjectName() + ": " + score + "/" + examToTake.getQuestions().size());
        currentStudent.setExamScore(selectedSubjectForExam.getSubjectId(), score); // Save the score
        System.out.println("Your result has been saved.");
    }

    /**
     * Allows a student to view their own exam results.
     * Delegates to the shared `displayStudentResults` helper method.
     * @param currentStudent The Student object currently logged in.
     */
    public void viewMyExamResult(Student currentStudent) {
        displayStudentResults(currentStudent);
    }
}
