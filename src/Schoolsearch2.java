import java.io.*;
import java.util.Scanner;

public class Schoolsearch2 {
    public static void main(String[] args) {
        int choice, exit=1;
        File list = new File("list.txt");
        File teachers = new File("teachers.txt");
        if(teachers.exists() && list.exists()){
            System.out.println("File "+teachers.getName()+" and "+list.getName()+" opened successfully");
        } else{
            System.out.println("File not found!");
            exit=0;
        }
        Scanner scanner = new Scanner(System.in);
        while(exit!=0){
            System.out.println("""
                    Choose option by what to find info:
                    1)Student
                    2)Teacher
                    3)Classroom
                    4)Bus
                    Additional tasks
                    5)Students in grade
                    6)Teachers by classroom
                    7)Teachers by grade
                    0)Exit
                    """);
            choice = scanner.nextInt();
            scanner.nextLine(); // додатковий виклик для споживання зайвого символу \n
            switch (choice){
                case 1://Пошук по прізвищу
                    System.out.println("Enter student`s last name:");
                    String lastname = scanner.nextLine();
                    searchByLastname(lastname);
                    break;
                case 2://Пошук за викладачем
                    System.out.println("Enter teacher`s last name:");
                    String teacher = scanner.nextLine();
                    searchByTeacher(teacher);
                    break;
                case 3://Пошук по прізвищу
                    System.out.println("Enter classroom`s number:");
                    String classroom = scanner.nextLine();
                    searchByClassroom(classroom);
                    break;
                case 4://Пошук за викладачем
                    System.out.println("Enter bus`s number:");
                    String bus = scanner.nextLine();
                    searchByBus(bus);
                    break;
                case 5://пошук за грейдом
                    System.out.println("Enter grade number:");
                    String grade = scanner.nextLine();
                    searchByGrade(grade);
                    break;
                case 6://пошук за классрумом вчителів
                    System.out.println("Enter classroom number:");
                    String classroom2 = scanner.nextLine();
                    searchByClassroomTeachers(classroom2);
                    break;
                case 7://пошук за грейдом вчителів
                    System.out.println("Enter grade number:");
                    String grade2 = scanner.nextLine();
                    searchByGradeTeachers(grade2);
                    break;
                case 0:
                    System.out.println("Bye-bye");
                    exit=0;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }

    }

    // LIST: 0StLastName, 1StFirstName, 2Grade, 3Classroom, 4Bus
    // TEACHERS: 0TLastName, 1TFirstName, 2Classroom
    private static void searchByLastname(String lastname){
        String line_student, line_teacher;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose variant(student info = 1 / student bus = 2)");
        int option = scanner.nextInt();
        scanner.nextLine();// додатковий виклик для споживання зайвого символу \n
        if(option==1){
            try {
                long starting_time = System.currentTimeMillis();
                BufferedReader list_reader = new BufferedReader(new FileReader("list.txt"));
                //Прохід по всьому файлу
                while ((line_student = list_reader.readLine()) != null){ //поки не досягнемо кінця файлу
                    //розділ рядків на слова
                    String[] parts_student = line_student.split(",");
                    //Пошук співпадінь прізвищ
                    if(parts_student[0].equalsIgnoreCase(lastname)){//ігноруючи капіталізацію
                        System.out.println("\nStudent: "+parts_student[0]+" "+parts_student[1]+"  Class: "+
                                parts_student[3]+"\nTechers from class "+parts_student[3]+":" );
                        BufferedReader teacher_reader = new BufferedReader(new FileReader("teachers.txt"));
                        while((line_teacher = teacher_reader.readLine()) != null){
                            String [] parts_teacher = line_teacher.split(",");
                            if(parts_student[3].equalsIgnoreCase(parts_teacher[2])){
                                System.out.println(parts_teacher[0]);
                            }
                        }
                    }
                }
                long total_time = System.currentTimeMillis() - starting_time;
                System.out.println("\nTime it took to find: "+total_time+"ms\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else{
            try {

                System.out.println("Enter bus number:");
                String bus = scanner.nextLine();
                long starting_time = System.currentTimeMillis();
                BufferedReader list_reader = new BufferedReader(new FileReader("list.txt"));
                //Прохід по всьому файлу
                while ((line_student = list_reader.readLine()) != null){ //поки не досягнемо кінця файлу
                    //розділ рядків на слова
                    String[] parts_student = line_student.split(",");
                    //Пошук співпадінь прізвищ
                    if(parts_student[0].trim().equalsIgnoreCase(lastname) && parts_student[4].trim().equalsIgnoreCase(bus)){//ігноруючи капіталізацію
                        System.out.println("Student: "+parts_student[0]+" "+parts_student[1]+"  Bus: "+
                                parts_student[4]);
                    }
                }
                long total_time = System.currentTimeMillis() - starting_time;
                System.out.println("\nTime it took to find: "+total_time+"ms\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    // LIST: 0StLastName, 1StFirstName, 2Grade, 3Classroom, 4Bus
    // TEACHERS: 0TLastName, 1TFirstName, 2Classroom
    private static void searchByTeacher(String teacher){
        String line_list, line_teachers;
        try {
            long starting_time = System.currentTimeMillis();
            BufferedReader teachers_reader = new BufferedReader(new FileReader("teachers.txt"));

            //Пошук вчителя у файлі
            while((line_teachers = teachers_reader.readLine())!=null){
                // розділ лайну на частини
                String[] parts_teacher = line_teachers.split(",");
                //пошук класрума по прізвищу
                if(parts_teacher[0].equalsIgnoreCase(teacher)){
                    //запам'ятовуємо класрум
                    String classroom = parts_teacher[2];
                    System.out.println("\n"+parts_teacher[0]+" "+ parts_teacher[1]+"`s students from classroom"+parts_teacher[2]+":");
                    //відкриваємо ліст в лупі, шоб кожен раз читання починалося спочатку і
                    // не пропускали студентів інших викладачів з однаковими прізвищами
                    BufferedReader list_reader = new BufferedReader(new FileReader("list.txt"));
                    //шукаємо по класруму вчителя студентів, які навчаються у цьому класрумі
                    while ((line_list = list_reader.readLine()) != null){

                        // розділ лайну на частини
                        String[] parts_student = line_list.split(",");
                        //якщо класрум студента співпадає з класрумом в пам'яті
                        if(parts_student[3].equalsIgnoreCase(classroom)){//ігноруючи капіталізацію
                            //виписуємо прізвище студента
                            System.out.println(parts_student[0]+" "+parts_student[1]);
                        }
                    }
                    list_reader.close();
                }
            }
            long total_time = System.currentTimeMillis() - starting_time;
            System.out.println("\nTime it took to find: "+total_time+"ms\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // LIST: 0StLastName, 1StFirstName, 2Grade, 3Classroom, 4Bus
    // TEACHERS: 0TLastName, 1TFirstName, 2Classroom
    private static void searchByClassroom(String classroom){
        String line;
        int i = 1;
        try {
            long starting_time = System.currentTimeMillis();
            BufferedReader list_reader = new BufferedReader(new FileReader("list.txt"));
            //Прохід по всьому файлу
            System.out.println(classroom + "`s students:");
            while ((line = list_reader.readLine()) != null){ //поки не досягнемо кінця файлу
                //розділ рядків на слова
                String[] parts = line.split(", ");
                //Пошук співпадінь прізвищ
                if(parts[3].equalsIgnoreCase(classroom)){//ігноруючи капіталізацію
                    System.out.println(i+")"+parts[0]+" "+parts[1]);
                    i++;
                }
            }
            long total_time = System.currentTimeMillis() - starting_time;
            System.out.println("\nTime it took to find: "+total_time+"ms\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // LIST: 0StLastName, 1StFirstName, 2Grade, 3Classroom, 4Bus
    // TEACHERS: 0TLastName, 1TFirstName, 2Classroom
    private static void searchByBus(String bus){
        String line;
        int i = 1;
        try {
            long starting_time = System.currentTimeMillis();
            BufferedReader reader = new BufferedReader(new FileReader("list.txt"));
            //Прохід по всьому файлу
            System.out.println(bus + "`s students:");
            while ((line = reader.readLine()) != null){ //поки не досягнемо кінця файлу
                //розділ рядків на слова
                String[] parts = line.split(", ");
                //Пошук співпадінь прізвищ
                if(parts[4].equalsIgnoreCase(bus)){//ігноруючи капіталізацію
                    System.out.println(i+")"+parts[0]+" "+parts[1]);
                    i++;
                }
            }
            long total_time = System.currentTimeMillis() - starting_time;
            System.out.println("\nTime it took to find: "+total_time+"ms\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // LIST: 0StLastName, 1StFirstName, 2Grade, 3Classroom, 4Bus
    // TEACHERS: 0TLastName, 1TFirstName, 2Classroom
    private static void searchByGrade(String grade) {
        String line;
        int i = 1;
        try {
            long starting_time = System.currentTimeMillis();
            BufferedReader reader = new BufferedReader(new FileReader("list.txt"));
            //Прохід по всьому файлу
            System.out.println("students in "+grade + "`s grade:");
            while ((line = reader.readLine()) != null){ //поки не досягнемо кінця файлу
                //розділ рядків на слова
                String[] parts = line.split(", ");
                //Пошук співпадінь прізвищ
                if(parts[2].equalsIgnoreCase(grade)){//ігноруючи капіталізацію
                    System.out.println(i+")"+parts[0]+" "+parts[1]);
                    i++;
                }
            }
            long total_time = System.currentTimeMillis() - starting_time;
            System.out.println("\nTime it took to find: "+total_time+"ms\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // LIST: 0StLastName, 1StFirstName, 2Grade, 3Classroom, 4Bus
    // TEACHERS: 0TLastName, 1TFirstName, 2Classroom
    private static void searchByClassroomTeachers(String classroom) {
        String line;
        int i = 1;
        try {
            long starting_time = System.currentTimeMillis();
            BufferedReader reader = new BufferedReader(new FileReader("teachers.txt"));
            //Прохід по всьому файлу
            System.out.println("Teachers in "+ classroom + " classroom:");
            while ((line = reader.readLine()) != null){ //поки не досягнемо кінця файлу
                //розділ рядків на слова
                String[] parts = line.split(", ");
                //Пошук співпадінь класрумів
                if(parts[2].equalsIgnoreCase(classroom)){//ігноруючи капіталізацію
                    System.out.println(i+")"+parts[0]+" "+parts[1]);
                    i++;
                }
            }
            long total_time = System.currentTimeMillis() - starting_time;
            System.out.println("\nTime it took to find: "+total_time+"ms\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // LIST: 0StLastName, 1StFirstName, 2Grade, 3Classroom, 4Bus
    // TEACHERS: 0TLastName, 1TFirstName, 2Classroom
    private static void searchByGradeTeachers(String grade) {
        String line_list, line_teachers;
        String[] classrooms = new String[100]; // Масив для збереження номерів класів
        int classroomCount = 0; // Лічильник класів
        try {
            long starting_time = System.currentTimeMillis();

            //Читання списку студентів і збереження номерів класів для вказаного грейду
            BufferedReader list_reader = new BufferedReader(new FileReader("list.txt"));

            while ((line_list = list_reader.readLine()) != null) {
                String[] parts_students = line_list.split(", ");
                if (parts_students[2].equalsIgnoreCase(grade)) {
                    String classroom = parts_students[3];

                    // Перевірка, чи класрум вже є у масиві
                    boolean alreadyExists = false;
                    for (int i = 0; i < classroomCount; i++) {
                        if (classrooms[i].equalsIgnoreCase(classroom)) {
                            alreadyExists = true;
                            break;
                        }
                    }

                    // Додавання класрума, якщо його ще немає в масиві
                    if (!alreadyExists) {
                        classrooms[classroomCount] = classroom;
                        classroomCount++;
                    }
                }
            }
            list_reader.close();

            //Пошук вчителів у збережених класах
            BufferedReader teachers_reader = new BufferedReader(new FileReader("teachers.txt"));
            System.out.println("Teachers for grade " + grade + ":");

            while ((line_teachers = teachers_reader.readLine()) != null) {
                String[] parts_teachers = line_teachers.split(",");

                // Перевірка чи викладає вчитель у будь-якому з класів для даного грейду
                for (int i = 0; i < classroomCount; i++) {
                    if (classrooms[i].equalsIgnoreCase(parts_teachers[2].trim())) {
                        System.out.println(parts_teachers[0] + " " + parts_teachers[1]); // Виведення імені вчителя
                    }
                }
            }
            teachers_reader.close();

            long total_time = System.currentTimeMillis() - starting_time;
            System.out.println("\nTime it took to find: " + total_time + "ms\n");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
