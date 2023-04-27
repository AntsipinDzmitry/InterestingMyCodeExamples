package SerializationExampleWithoutSerializable;
import java.io.*;
import java.util.*;

/*
Reading and writing to a file: do-it-yourself serialization without the help of serializeable

*/

public class SerializationExample {
    public static void main(String[] args) {
        try {
            File yourFile = File.createTempFile("my_file_name", null);
            OutputStream outputStream = new FileOutputStream(yourFile);
            InputStream inputStream = new FileInputStream(yourFile);

            UserPack userPack = new UserPack();
            // we Initialize the users field for the userPack object by creating
            // and adding two user objects here to test the functionality
            User user1 = new User();
            user1.setFirstName("Dima");
            user1.setLastName("Antipin");
            user1.setCountry(User.Country.UNITED_STATES);
            user1.setBirthDate(new GregorianCalendar(1988, Calendar.JUNE, 9).getTime());
            user1.setMale(true);

            User user2 = new User();
            user2.setFirstName("Hanna");
            user2.setLastName("Antipina");
            user2.setCountry(User.Country.UNITED_KINGDOM);
            user2.setBirthDate(new GregorianCalendar(1988, Calendar.OCTOBER, 16).getTime());
            user2.setMale(false);

            userPack.users.add(user1);
            userPack.users.add(user2);
            userPack.save(outputStream);
            outputStream.flush();

            UserPack loadedObject = new UserPack();
            loadedObject.load(inputStream);

            // Here check that the userPack object is equal to the loadedObject object
            System.out.println(userPack.equals(loadedObject));

            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Oops, something wrong with my file");
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Oops, something wrong with save/load method");
        }
    }

    public static class UserPack {
        public List<User> users = new ArrayList<>();

        public void save(OutputStream outputStream) throws Exception {

            /* the method checks whether the list of users is empty or not, and if it is not empty,
             then writes information about users to the file. I implement the method by creating a stringbuilder object
             for the convenience of writing strings, dates and nested enums, as well as boolean values.
             This allows me to achieve code readability and reduce code redundancy. */
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
                if (!this.users.isEmpty()) {
                    for (User currentUser : this.users) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(currentUser.getFirstName());
                        sb.append("/");
                        sb.append(currentUser.getLastName());
                        sb.append("/");
                        sb.append(currentUser.getBirthDate().getTime());
                        sb.append("/");
                        sb.append(currentUser.isMale());
                        sb.append("/");
                        sb.append(currentUser.getCountry());

                        writer.write(sb.toString());
                        writer.newLine();
                    }
                }
            }
        }
        public void load(InputStream inputStream) throws Exception {
            // Implement this method
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                while (reader.ready()) {
                    User user = new User();
                    String[] tempArray = reader.readLine().split("/");
                    if (tempArray.length < 5) {
                        break;
                    }
                    user.setFirstName(tempArray[0]);
                    user.setLastName(tempArray[1]);
                    user.setBirthDate(new Date(Long.parseLong(tempArray[2])));
                    user.setMale(Boolean.parseBoolean(tempArray[3]));
                    switch (tempArray[4]) {
                        case "UNITED_STATES":
                            user.setCountry(User.Country.UNITED_STATES);
                            break;
                        case "UNITED_KINGDOM":
                            user.setCountry(User.Country.UNITED_KINGDOM);
                            break;
                        default:
                            user.setCountry(User.Country.OTHER);
                            break;
                    }
                    this.users.add(user);
                }
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UserPack userPack = (UserPack) o;

            return users != null ? users.equals(userPack.users) : userPack.users == null;

        }

        @Override
        public int hashCode() {
            return users != null ? users.hashCode() : 0;
        }
    }
}
