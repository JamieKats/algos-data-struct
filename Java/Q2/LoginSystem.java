import jdk.jshell.spi.ExecutionControl;

import java.util.function.Function;

public class LoginSystem extends LoginSystemBase {
    /** TODO
     * - Can we use String.length? */

    private int initialSize = 101;
    UserInfo[] hashTable;

    private int numUsers;

    private int hashConstant = 31;

    private double loadFactor = 0.5;

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

//    public int hashHelper(String key) {
//         if (key.length() == 1) {
//             return key.charAt(0);
//         }
//         return hashHelper(key.substring(0, key.length() - 1)) * hashConstant + key.charAt(key.length() - 1);
//    }

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

        // Calculate hash of email and password
        int emailHash = this.hashCode(email);
        int passwordHash = this.hashCode(password);
        int emailIndex = emailHash % this.hashTable.length;

//        // check if user in table already
//        for (int i = 0; i < this.hashTable.length; i++) {
//            int probeLocation = (i + emailIndex) % this.hashTable.length;
//            UserInfo userInfo = this.hashTable[probeLocation];
//
//            // Check if user already in system
//            if (userInfo.getEmail().equals(email)) {
//                return false;
//            }
//        }

        // linear probe
        int probe = linearProbe(newUser);
        if (probe == -2) { // User already in system
            return false;
        }

        this.hashTable[probe] = newUser;
        this.numUsers++;
        return true;
    }

    /** The number of values in the hash table must be less than loadfactor * hashtable size */
    boolean arrayTooFull() {
        return (this.hashTable.length * this.loadFactor < this.numUsers);
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
            int probeLocation = (i + emailIndex) % this.hashTable.length;
            UserInfo probedUser = this.hashTable[probeLocation];

            if (probedUser == null) { // empty spot found
                // if encountered a DEL return the DEL location
                // otherwise return the null probe location
                if (deletedUserIndex != -1) { // a previous DEL location has been found
                    return deletedUserIndex;
                }
                return probeLocation; // Return the probelocation if no previous DEL found
            }

            // If you find the first deleted user set deletedUserIndex
            if (deletedUserIndex == -1 && probedUser.getIsDeleted()) {
                deletedUserIndex = probeLocation;
            }

            // if you find the same user already in the system return -2
            if (probedUser.equals(user)) {
                return -2;
            }
        }
        return -1; // no valid locations have been found
    }

    public void growArrayTripleStrategy() {
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

            // For each user in the hashtable check the provided email matches the plain text email
            // and hashed password in the hashtable
//            if (userInfo.getEmail().equals(email)
//                && userInfo.getPasswordHash() == this.hashCode(password)) {
            if (probedUser.equals(userToRemove)) {
                this.hashTable[probeLocation].setUserAsDeleted(); // remove user
                this.numUsers--;
                return true;
            }

            // Finding a null means the user hasnt been inserted
            if (this.hashTable[probeLocation] == null) {
                break;
            }
        }
        return false;
    }



    @Override
    public int checkPassword(String email, String password) {
        /* Add your code here! */
        int hashedEmail = this.hashCode(email);
        int emailIndex = hashedEmail % this.hashTable.length;
        for (int i = 0; i < this.hashTable.length; i++) {
            int probeLocation = (i + emailIndex) % this.hashTable.length;
            UserInfo userInfo = this.hashTable[probeLocation];

            // User is in the system and password is correct
            if (userInfo.getEmail().equals(email)
                && userInfo.getPasswordHash() == this.hashCode(password)) {
                return probeLocation;
            } else if (userInfo.getEmail().equals(email)
                    && userInfo.getPasswordHash() != this.hashCode(password)) {
                // User is in system but password is incorrect
                return -2;
            }
        }
        // User not in the system
        return -1;
    }

    @Override
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        /* Add your code here! */
        int hashedEmail = this.hashCode(email);
        int emailIndex = hashedEmail % this.hashTable.length;
        for (int i = 0; i < this.hashTable.length; i++) {
            int probeLocation = (i + emailIndex) % this.hashTable.length;
            UserInfo userInfo = this.hashTable[probeLocation];

            // If user in system and old password == password in system
            if (userInfo.getEmail().equals(email)
                    && userInfo.getPasswordHash() == this.hashCode(oldPassword)) {
                // change password
                userInfo.setPasswordHash(this.hashCode(newPassword));
                return true;
            } else if (userInfo.getEmail().equals(email)
                    && userInfo.getPasswordHash() != this.hashCode(oldPassword)) {
                return false;
            }
        }
        // user could not be found
        return false;
    }

    /* Add any extra functions below */

    public static void main(String[] args) {
        /*
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * REMOVE THE MAIN METHOD BEFORE SUBMITTING TO THE AUTOGRADER
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * The following main method is provided for simple debugging only
         */
        LoginSystem loginSystem = new LoginSystem();
        System.out.println("hello".substring(0, 5-1));
        System.out.println((int)'c');
        System.out.println(loginSystem.hashCode("abc"));
        assert loginSystem.hashCode("GQHTMP") == loginSystem.hashCode("H2HTN1");
        assert loginSystem.size() == 101;

        assert loginSystem.checkPassword("a@b.c", "L6ZS9") == -1;
        loginSystem.addUser("a@b.c", "L6ZS9");
        assert loginSystem.checkPassword("a@b.c", "ZZZZZZ") == -2;
        assert loginSystem.checkPassword("a@b.c", "L6ZS9") == 94;
        loginSystem.removeUser("a@b.c", "L6ZS9");
        assert loginSystem.checkPassword("a@b.c", "L6ZS9") == -1;
    }
}

class UserInfo implements Comparable<UserInfo> {

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

//    /** Returns 1 if the users are the same 0 if they are not the same */
//    public int compareTo(UserInfo userInfo) {
//        if (this.getEmail().equals(userInfo.getEmail())
//                && this.getPasswordHash() == userInfo.getPasswordHash()
//                && this.getIsDeleted() == userInfo.getIsDeleted()) {
//            return 1;
//        }
//        return 0;
////        throw new ExecutionControl.NotImplementedException("UserInfo compareTo not implemented!")
//    }

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
                && this.getIsDeleted() == user.getIsDeleted();
    }
}
