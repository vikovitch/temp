import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.*;
import java.util.Collections;
import java.util.Comparator;
import java.io.*;

public final class Main {
   static Scanner scanObj = new Scanner(System.in);

  static void myMethod() {
    task.print("Method ran");
  }

  static void GPTChallenge1() {
    ArrayList<Integer> numbers = new ArrayList<Integer>();
    boolean doneReceived = false;
    while (doneReceived == false) {
      String input = scanObj.nextLine();
      if (input.toLowerCase().equals("done")) {
        doneReceived = true;
      } else {
        try {
          numbers.add(Integer.parseInt(input));
        } catch (Exception e) {
          task.print("Input was not a number or \"done\".");
        }
      }
    }
    
    task.print("Total count of numbers inputted is " + numbers.size());

    if (numbers.size() > 0) {
      int sum = 0;
      for (int v : numbers) {
        sum += v;
      }
      task.print("The sum of the numbers is " + sum);

      int average = sum / numbers.size();
      task.print("The average of the numbers is " + average);

      int smallest = numbers.get(0);
      int largest = numbers.get(0);
      var itr1 = numbers.iterator();
      while (itr1.hasNext()) {
        int v = itr1.next();
        if (v < smallest) {
          smallest = v;
        } else if (v > largest) {
          largest = v;
        }
      }
      task.print("Smallest number is " + smallest + " and largest is " + largest + ".");
    } else {
      task.print("No numbers given, cannot give sum, average or smallest/largest.");
    }
  }

  static void GPTChallenge2() {
    HashMap<String, Integer> wordsfrequency = new HashMap<String, Integer>();
    boolean finished = false;
    while (finished == false) {
      String input = scanObj.nextLine();
      if (input.equals("END")) {
        finished = true;
      } else {
        input = input.toLowerCase();
        Pattern pattern = Pattern.compile(" ");
        String[] seperatedinput = pattern.split(input);
        for (int i = 0; i < seperatedinput.length; i++) {
          String v = seperatedinput[i];
          if (i == 0) {input = "";}
          input = input + v + ",";
        }
        pattern = Pattern.compile(",");
        seperatedinput = pattern.split(input);
        for (int i = 0; i < seperatedinput.length; i++) {
          String v = seperatedinput[i];
          if (wordsfrequency.containsKey(v) == false) {
            wordsfrequency.put(v, 1);
          } else {
            wordsfrequency.put(v, wordsfrequency.get(v) + 1);
          }
        }
      }
    }
    ArrayList<String> sortlist = new ArrayList<String>();
    for (String i : wordsfrequency.keySet()) {
      sortlist.add(i);
    }
    Collections.sort(sortlist);
    for (String key : sortlist) {
      int freq = wordsfrequency.get(key);
      task.print(key + " appeared " + freq + " times.");
    }
  }

  static void GPTChallenge3() {
    HashMap<String, ArrayList<Float>> studentData = new HashMap<String, ArrayList<Float>>();
    String selectedStudent = "";
    boolean exit = false;
    while (exit == false) {
      task.print("Enter student name (or \"add student\" + name), \"view all students\", \"view highest grade\" or \"EXIT\":");
      String input = scanObj.nextLine();
      if (input.toLowerCase().startsWith("add student ") && input.length() > 12) {
        selectedStudent = input.toLowerCase().substring(12);
        studentData.put(selectedStudent, new ArrayList<Float>());
        task.print("Added student " + selectedStudent);
        input = selectedStudent;
      }
      if (studentData.keySet().contains(input.toLowerCase())) {
        selectedStudent = input.toLowerCase();
        boolean exit1 = false;
        while (exit1 == false) {
          task.print("What to do with " + selectedStudent +
            " (\"add grade # #\" (grade, index), \"see grade #\" (index), \"see grade average\" or \"BACK\"):");
          String input1 = scanObj.nextLine();
          if (input1.toLowerCase().startsWith("add grade ")) {
            try {
              float passedValue0 = Float.parseFloat(input1.substring(10).split(" ")[0]);
              int passedValue1 = Integer.parseInt(input1.substring(10).split(" ")[1]);
              if (input1.substring(10).split(" ").length > 2) {
                task.print("Not a valid input.");
                continue;
              }
              studentData.get(selectedStudent).add(passedValue1, passedValue0);
              task.print("Grade added.");
            } catch (Exception e) {
              task.print("Not a valid input.");
            }
          } else if (input1.toLowerCase().startsWith("see grade ") && !input1.toLowerCase().equals("see grade average")) {
            try {
              int passedValue = Integer.parseInt(input1.substring(10));
              task.print("Grade " + passedValue + " is " + studentData.get(selectedStudent).get(passedValue) + ".");
            } catch (Exception e) {
              task.print("Invalid input.");
            }
          } else if (input1.toLowerCase().equals("see grade average")) {
            if (studentData.get(selectedStudent).size() == 0) {
              task.print("This student has no grades.");
            } else {
              ArrayList<String> gradeString = new ArrayList<String>();
              gradeString.add(0, "");
              ArrayList<Float> gradeAverage = new ArrayList<Float>();
              gradeAverage.add(0, 0f);
              studentData.get(selectedStudent).forEach(v -> {
                gradeAverage.add(0, gradeAverage.get(0) + v);
                gradeString.add(0, gradeString.get(0) + (gradeString.get(0).length()==0?"":", ") + v);
              });
              task.print("All grades are:");
              task.print(gradeString.get(0));
              task.print("And the average is " + gradeAverage.get(0) / studentData.get(selectedStudent).size() + ".");
            }
          } else if (input1.equals("BACK")) {
            task.print("Returning...");
            selectedStudent = "";
            exit1 = true;
          } else {
            task.print("Not a valid input.");
          }
        }
      } else {
        switch (input.toLowerCase()) {
          case "view all students":
            if (studentData.keySet().size() == 0) {
              task.print("no students registered.");
            } else {
              task.print("all students are:");
              studentData.keySet().forEach(v -> task.print(v));
            }
            break;
          case "view highest grade":
            if (studentData.keySet().size() == 0) {
              task.print("no students registered.");
            } else {
              ArrayList<Float> highest = new ArrayList<>();
              highest.add(0, 0f);
              HashSet<String> student = new HashSet<String>();
              studentData.keySet().forEach(key -> {
                ArrayList<Float> val = studentData.get(key);
                val.forEach(v -> {
                  if (v.equals(highest.get(0))) {
                    student.add(key);
                  } else if (v > highest.get(0)) {
                    highest.add(0, v);
                    student.clear();
                    student.add(key);
                  }
                });
              });
              task.print("highest grade" + (student.size()>1?"s are ":" is ") + highest.get(0) + " by:");
              student.forEach(v -> task.print(v));
            }
            break;
          default:
            if (input.equals("EXIT")) {
              task.print("exiting...");
              exit = true;
            } else {
              task.print("Invalid input.");
            }
        }
      }
    }
  }

  public static void main(String[] args) {
    if (args.length > 0 && args[0].equals("--dofull")) {
      task.print("input boinkuss for da date");
      String input = scanObj.nextLine();
      if (input.equals("boinkuss")) {
        task.print("Da date iz " + DateTimeFormatter.ofPattern("MMMM dd, yyyy").format(LocalDateTime.now()));
      } else {
        task.print("oh :(");
      }


      PrivateClass.AddValue( "3");
      task.print(PrivateClass.GetValue(0));

      // linkedlist is just arraylist but for a different purpose or smth

      String[] thing = {"test0", "test1"};
      for (int i = 0; i < thing.length; i++) {
        var v = thing[i];
        task.print(v == "test0" ? "yaa" : "nooo");
      }

      HashMap<String, String> hashmaptest = new HashMap<String, String>();
      hashmaptest.put("Netherlands", "Amsterdam");
      hashmaptest.put("England", "London");
      for (String i : hashmaptest.keySet()) {
        String v = hashmaptest.get(i);
        task.print(i + ", " + v);
      }

      Iterator<String> itr = hashmaptest.values().iterator();
      while (itr.hasNext()) {
        task.print(itr.next());
      }

      HashSet<String> hashsettest = new HashSet<String>();
      hashsettest.add("bing");
      hashsettest.add("bong");
      hashsettest.add("ding");
      hashsettest.add("dong");

      Integer myInttst = 35;
      task.print(myInttst.toString().length());

      try {
        int[] hereo = {1, 2, 3};
        task.print(hereo[3]);
      } catch (Exception e) {
        // throw new ArithmeticException("except it errur");
      } finally {
        task.print("we done with da try staetmnt");
      }

      GPTChallenge1();

      Pattern pattern = Pattern.compile("blingg");
      Matcher matcher = pattern.matcher(scanObj.nextLine());
      if (matcher.find()) {
        task.print("blingg found");
      } else {
        task.print("blingg not found");
      }

      new Thread(() -> {task.print("Seperate thread achieved");}).start();

      Runnable arrowfunc = () -> {task.print("Lambda expression called");};
      arrowfunc.run();
      
      PrivateClass.AddValue("1");
      PrivateClass.AddValue("2");
      PrivateClass.GetFull().forEach(v -> System.out.print(v));
      PrivateClass.SortList();
      PrivateClass.GetFull().forEach(v -> System.out.print(v));
      task.print("");

      Main obj1 = new Main(3, 1);
      Main obj2 = new Main(1, 2);
      Main obj3 = new Main(2, 3);
      ArrayList<Main> objArrat = new ArrayList<Main>();
      objArrat.add(obj1);
      objArrat.add(obj3);
      objArrat.add(obj2);
      ArrayList<Main> objArrat1 = objArrat;
      Collections.sort(objArrat, new CompareForObjArrat());
      objArrat.forEach(v -> task.print(v.zz));
      Collections.sort(objArrat1, (oblyat1, oblyat2) -> {
        return oblyat1.xy - oblyat2.xy;
      });
      objArrat.forEach(v -> task.print(v.zz));
    }

    File txt = new File("testfile.txt");
    String wantedtext = """
      Text in a text file
      this is line 2

      that skipped a line
      """;
    try {
      if (txt.createNewFile()) {
        task.print("file created");
      } else {
        task.print("file could not be created");
      }
    } catch (IOException e) {
      task.print("something went rong");
      System.out.println(e);
    }
    if (txt.canRead() && txt.canWrite()) {
      String fullcontents = "";
      String formattedWantedText = "";
      try {
        Scanner fileReadObj = new Scanner(txt);
        while (fileReadObj.hasNextLine()) {
          fullcontents += fileReadObj.nextLine();
        }
        fileReadObj.close();
        Scanner transformWantedTextObj = new Scanner(wantedtext);
        while (transformWantedTextObj.hasNextLine()) {
          formattedWantedText += transformWantedTextObj.nextLine();
        }
        transformWantedTextObj.close();
      } catch (FileNotFoundException e) {}
      if (!fullcontents.equals(formattedWantedText)) {
        try {
          FileWriter txtw = new FileWriter("testfile.txt");
          txtw.write(wantedtext);
          txtw.close();
          task.print("written to file");
        } catch (IOException e) {}
      } else {
        task.print("could not write or was not needed");
      }
    }
    try {
      if (args.length > 0 && args[0].equals("--deletefile")) {
        if (txt.delete()) {
          task.print("deleted " + txt.getName());
        } else {
          task.print("could not delete file");
        }
      }
    } catch (SecurityException e) {}

    GPTChallenge3();

  }

  int xy;
  int zz;
  Main(int value, int creationorder) {
    xy = value;
    zz = creationorder;
  }
}

class CompareForObjArrat implements Comparator<Main> {
  public int compare(Main obj1, Main obj2) {
    return obj1.xy - obj2.xy;
  }
}

class PrivateClass {
  private static ArrayList<String> bingbong = new ArrayList<String>();

  static String GetValue(int index) {
    return bingbong.get(index);
  }
  static void SetValue(int index, String value) {
    bingbong.set(index, value);
  }
  static void AddValue(String value) {
    bingbong.add(value);
  }
  static void RemoveValue(int index) {
    bingbong.remove(index);
  }
  static ArrayList<String> GetFull() {
    return bingbong;
  }
  static void ClearFull() {
    bingbong.clear();
  }
  static int GetSize() {
    return bingbong.size();
  }
  static void SortList() {
    Collections.sort(bingbong);
  }
}

enum HumanoidStateType {
  Default,
  GettingUp,
  Dead;

  static void enumMethod() {
    task.print("enum method fired");
  }
}