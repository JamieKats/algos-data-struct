import jdk.jshell.spi.ExecutionControl;

import java.util.function.Function;

public class LoginSystem extends LoginSystemBase {
    /** TODO
     * - Can we use String.length? */

    private int initialSize = 101;
//    private int initialSize = 5;
    UserInfo[] hashTable;

    private int numUsers;

    private int hashConstant = 31;

    private double loadFactor = 0.5;
//    private double loadFactor = 1;

    LoginSystem() {
        this.hashTable = new UserInfo[initialSize];
        this.numUsers = 0;
    }
    @Override
    public int size() {
        /* Add your code here! */
        return hashTable.length;
    }

    @Override
    public int getNumUsers() {
        /* Add your code here! */
        return numUsers;
    }

    @Override
    public int hashCode(String key) {
        /* Add your code here! */
        if (key.length() == 1) {
            return key.charAt(0);
        }
        return this.hashCode(key.substring(0, key.length() - 1)) * hashConstant + key.charAt(key.length() - 1);
    }

    /** Need to scan whole map to make sure user not already in the table
     * TODO ask if the addUser must always scan entire table
     * TODO how do you know there hasnt been a collision so the user was put in a different spot
     * TODO then the original spot is freed up since the user is removed and when checking if
     * TODO the user is in the system you incorrectly return true since you only checked the
     * TODO index the user is suppose to be in but not other indexes that the user may have been
     * TODO put in due to linear probing???*/
    @Override
    public boolean addUser(String email, String password) {
        /* Add your code here! */
        // Check if hash table needs to be resized
        if (arrayTooFull()) {
            growArrayTripleStrategy();
        }
        UserInfo newUser = new UserInfo(email, this.hashCode(password));

        // linear probe
        int probe = linearProbe(newUser);
        if (probe == -2 || probe == -3) { // User already in system with same or different password
            return false;
        }

        this.hashTable[probe] = newUser;
        this.numUsers++;
        return true;
    }

    /** The number of values in the hash table must be less than loadfactor * hashtable size */
    boolean arrayTooFull() {
//        System.out.println(this.hashTable.length * this.loadFactor);
//        System.out.println(this.getNumUsers());
        return (this.hashTable.length * this.loadFactor <= this.getNumUsers());
    }

    /** TODO pass function callback into linearprobe that will handle what to do if you find the
     * user already in the hashtable e.g. removeuser, addUser, changePassword will all do
     * linearprobing but will have different functionality
     *
     * Can linear probe and return the index of the next available slot or DEL
     *
     * if linear probing for a key you either find
     * 1. they key already in the hashtable: return -1
     * 2. an empty slot or a DEL slot: return the index
     *
     * The number of values will always be > load factor * size of table
     * */
    public int linearProbe(UserInfo user) {
        int emailHash = this.hashCode(user.getEmail());
        int emailIndex = emailHash % hashTable.length;

        int deletedUserIndex = -1;
        for (int i = 0; i < this.hashTable.length; i++) {
//            System.out.println("probing...");
            int probeLocation = (i + emailIndex) % this.hashTable.length;
            UserInfo probedUser = this.hashTable[probeLocation];

            if (probedUser == null) { // empty spot found
                if (deletedUserIndex != -1) { // a previous DEL location has been found
                    return deletedUserIndex;
                }
                return probeLocation; // Return the probelocation if no previous DEL found
            }

            // If you find the first deleted user set deletedUserIndex
            if (deletedUserIndex == -1 && probedUser.getIsDeleted()) {
                deletedUserIndex = probeLocation;
                continue;
            }

            // if you find the same user and password already in the system return -2
            if (probedUser.equals(user)) {
                return -2;
            }

            // if you find same user but different password return -3
            if (probedUser.sameUserWrongPassword(user)) {
                return -3;
            }
        }
        if (deletedUserIndex != -1) { // if whole table traversed return the index of the first
            // DEL one
            return deletedUserIndex;
        }
        return -1; // no valid locations have been found
    }

    public void growArrayTripleStrategy() {
//        System.out.println("Growing array...");
        int oldHashTableSize = this.hashTable.length;
        int newHashTableSize = oldHashTableSize * 3;
        UserInfo[] oldHashTable = this.hashTable;
        this.hashTable = new UserInfo[newHashTableSize];

        // for each user in the hash table rehash and compress the email and put it into the new
        // hashtable
        for (int i = 0; i < oldHashTable.length; i++) {
            if (oldHashTable[i] == null) { continue; }
            UserInfo userOldTable = oldHashTable[i];

            // Rehash and compress email with new array size
            // allowing this code since emails will be limited in size Ed Post #207
            int emailHash = this.hashCode(userOldTable.getEmail());
            int emailIndex = emailHash % this.hashTable.length;

            // Insert email into new hash table
            // TODO call add() for new array rather than rewrite the logic
            for (int j = 0; j < this.hashTable.length; j++) {
                int probeLocation = (j + emailIndex) % this.hashTable.length;

                // If we find empty spot put user in and break out of inner loop
                if (this.hashTable[probeLocation] == null) {
                    this.hashTable[probeLocation] = userOldTable;
                    break;
                }
            }
        }
    }

    /** Set the user isDeleted to true */
    @Override
    public boolean removeUser(String email, String password) {
        /* Add your code here! */
        UserInfo userToRemove = new UserInfo(email, this.hashCode(password));
        int hashedEmail = this.hashCode(email);
        int emailIndex = hashedEmail % this.hashTable.length;
        for (int i = 0; i < this.hashTable.length; i++) {
            int probeLocation = (i + emailIndex) % this.hashTable.length;
            UserInfo probedUser = this.hashTable[probeLocation];

            // Finding a null means the user hasnt been inserted
            if (probedUser == null) {
                break;
            }

            // For each user in the hashtable check the provided email matches the plain text email
            // and hashed password in the hashtable
            if (probedUser.equals(userToRemove)) {
                this.hashTable[probeLocation].setUserAsDeleted(); // remove user
                this.numUsers--;
                return true;
            }
        }
//        System.out.println("removeUser: user not found...");
        return false; // user not found in hash table
    }



    @Override
    public int checkPassword(String email, String password) {
        /* Add your code here! */
        UserInfo user = new UserInfo(email, this.hashCode(password));
        int hashedEmail = this.hashCode(email);
        int emailIndex = hashedEmail % this.hashTable.length;
        for (int i = 0; i < this.hashTable.length; i++) {
            int probeLocation = (i + emailIndex) % this.hashTable.length;
            UserInfo userProbed = this.hashTable[probeLocation];

            // If null is found the user is not in the system
            if (userProbed == null) {
                break;
            }

            // User is in the system and password is correct
            if (userProbed.equals(user)) {
                return probeLocation;
            } else if (userProbed.sameUserWrongPassword(user)) {
                return -2; // User is in system but password is incorrect
            }
        }
        return -1; // User not in the system
    }

    @Override
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        /* Add your code here! */
        int hashedEmail = this.hashCode(email);
        int emailIndex = hashedEmail % this.hashTable.length;
        UserInfo oldUser = new UserInfo(email, this.hashCode(oldPassword));

        for (int i = 0; i < this.hashTable.length; i++) {
//            System.out.println("probing changepassword...");
            int probeLocation = (i + emailIndex) % this.hashTable.length;
            UserInfo probedUser = this.hashTable[probeLocation];

            if (probedUser == null) { // user could not be found
//                System.out.println("changePassword: User not found");
                break;
            }

            // If user in system and old password == password in system
            if (probedUser.equals(oldUser)) {
                // change password
//                System.out.println("Changing passwords");
                this.hashTable[probeLocation].setPasswordHash(this.hashCode(newPassword));
                return true;
            } else if (probedUser.sameUserWrongPassword(oldUser)) { // old password incorrect
                break;
            }
        }
        // user could not be found
        return false;
    }

    /* Add any extra functions below */

//    public void printTable() {
//        System.out.println("/// HASH TABLE ///");
//        for (int i = 0; i < this.hashTable.length; i++) {
//            if (this.hashTable[i] == null) {
//                System.out.println("null");
//                continue;
//            }
//            UserInfo userInfo = this.hashTable[i];
//            System.out.println(userInfo.getEmail() + " : " + userInfo.getPasswordHash() + " : " + userInfo.getIsDeleted());
//        }
//        System.out.println("/// HASH TABLE END ///");
//    }

//    public static void main(String[] args) {
//        /*
//         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//         * REMOVE THE MAIN METHOD BEFORE SUBMITTING TO THE AUTOGRADER
//         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//         * The following main method is provided for simple debugging only
//         */
//        LoginSystem loginSystem = new LoginSystem();
//        System.out.println("hello".substring(0, 5-1));
//        System.out.println((int)'c');
//        System.out.println(loginSystem.hashCode("abc"));
//        assert loginSystem.hashCode("GQHTMP") == loginSystem.hashCode("H2HTN1");
////        assert loginSystem.size() == 101;
//
//        assert loginSystem.checkPassword("a@b.c", "L6ZS9") == -1;
//        loginSystem.addUser("a@b.c", "L6ZS9");
//        System.out.println("num users " + loginSystem.numUsers);
//        loginSystem.printTable();
//        assert loginSystem.checkPassword("a@b.c", "ZZZZZZ") == -2;
////        assert loginSystem.checkPassword("a@b.c", "L6ZS9") == 94;
//        loginSystem.removeUser("a@b.c", "L6ZS9");
//        assert loginSystem.checkPassword("a@b.c", "L6ZS9") == -1;
//
//        // add more users
//        loginSystem.addUser("h@h.c", "hello");
//        loginSystem.printTable();
//
//        loginSystem.addUser("b@b.c", "world");
//        loginSystem.printTable();
//
//        loginSystem.addUser("w@w.c", "password");
//        loginSystem.printTable();
//
//        loginSystem.addUser("w@a.c", "password1");
//        loginSystem.printTable();
//        System.out.println("num users " + loginSystem.numUsers);
//        loginSystem.addUser("g@a.c", "pass");
//        loginSystem.printTable();
//
//        loginSystem.addUser("x@a.c", "2HwK");
//        loginSystem.printTable();
//
//        loginSystem.addUser("x@x.c", "henw");
//        loginSystem.printTable();
//        System.out.println("num users " + loginSystem.numUsers);
//        // 10th user
//        loginSystem.addUser("x@3.c", "edhenw");
//        loginSystem.printTable();
//
//        loginSystem.addUser("q@3.c", "crikey");
//        loginSystem.printTable();
//
//        System.out.println("table size " + loginSystem.size());
//        loginSystem.addUser("p@3.c", "C0N");
//        loginSystem.printTable();
//
//        // table should grow
//        loginSystem.addUser("e@3.c", "CON");
//        loginSystem.printTable();
//
//        // try check password for user
//        System.out.println("user e@3.c at location " + loginSystem.checkPassword("e@3.c", "CON"));
//
//        loginSystem.removeUser("e@3.c", "CON");
//        loginSystem.printTable();
//        System.out.println("num users " + loginSystem.numUsers);
//
//        // try check password for deleted user
//        assert loginSystem.checkPassword("e@3.c", "CON") == -1;
//
//        // check user that never existed
//        assert loginSystem.checkPassword("bad", "CON") == -1;
//
//        // check user wrong pass
//        assert loginSystem.checkPassword("h@h.c", "hello1") == -2;
//
//        // change password
//        loginSystem.changePassword("h@h.c", "hello", "hello1");
//        loginSystem.printTable();
//
//        // change password wrong old
//        assert loginSystem.changePassword("h@h.c", "hello", "hello1") == false;
//
//        // change password successful
//        assert loginSystem.changePassword("h@h.c", "hello1", "helloworld") == true;
//        loginSystem.printTable();
//        // change password user doesnt exist
//        assert loginSystem.changePassword("aaaaa", "hello", "hello1") == false;
//
//        // REMOVE USER TESTS
//
//        // remove user not found
//        assert loginSystem.removeUser("aaaaa", "bbbb") == false;
//
//        // remove user wrong pass
//        assert loginSystem.removeUser("q@3.c", "bbbb") == false;
//
//        // remove user correct pass
//        assert loginSystem.removeUser("q@3.c", "crikey") == true;
//        loginSystem.printTable();
//
//        // LINEAR PROBE TEST
//
//        // probe user exists
//        System.out.println("b@b.c probe =  " + (loginSystem.linearProbe(new UserInfo("b@b.c",
//            loginSystem.hashCode("world")))));
//
//        // probe user already in system
//        assert loginSystem.linearProbe(new UserInfo("g@a.c", loginSystem.hashCode("pass"))) == -2;
//
//        // probe user previously deleted
//        loginSystem.printTable();
//        System.out.println("user would be inserted at location: " + (loginSystem.linearProbe(new UserInfo(
//                "q@3.c",
//                loginSystem.hashCode("big")))));
//        loginSystem.printTable();
//
//        // ADD USER TESTS
//
//        // add user already in system
//        assert loginSystem.addUser("g@a.c", "pass") == false;
//
//        // add user previously deleted
//        assert (loginSystem.addUser("q@3.c", "big"));
//        loginSystem.printTable();
//
//        loginSystem.removeUser("q@3.c", "big");
//
//        // add user goed in previously deleted spot in front
//        assert (loginSystem.addUser("x@x.c", "gib")) == false;
//        loginSystem.printTable();
//    }
}

class UserInfo {

    /** Plaintext email stored for user otherwise if only a hashed email was stored you could end
     * up with users with the same email hash and password hash */
    private String email;

    /** Password hash stored for user */
    private int passwordHash;

    private boolean isDeleted;

    public UserInfo(String email, int passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.isDeleted = false;
    }

    public String getEmail() {
        return this.email;
    }

    public int getPasswordHash() {
        return this.passwordHash;
    }

    public void setPasswordHash(int newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }

    public void setUserAsDeleted() {
        this.isDeleted = true;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    public boolean sameUserWrongPassword(UserInfo user) {
        return this.getEmail().equals(user.getEmail())
                && this.getPasswordHash() != user.getPasswordHash()
                && this.getIsDeleted() == user.getIsDeleted()
                && !this.getIsDeleted();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof UserInfo)) {
            return false;
        }

        UserInfo user = (UserInfo) obj;

        return this.getEmail().equals(user.getEmail())
                && this.getPasswordHash() == user.getPasswordHash()
                && this.getIsDeleted() == user.getIsDeleted()
                && !this.getIsDeleted();
    }
}
