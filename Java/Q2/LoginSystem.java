import java.util.function.Function;

public class LoginSystem extends LoginSystemBase {
    /** TODO
     * - Can we use String.length? */

    private int initialSize = 101;
    UserInfo[] hashTable;

    private int numUsers;

    private int hashConstant = 31;

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
        if (this.numUsers == hashTable.length) {
            growArrayTripleStrategy();
        }
        UserInfo newUser = new UserInfo(email, this.hashCode(password));

        // Calculate hash of email and password
        int emailHash = this.hashCode(email);
        int passwordHash = this.hashCode(password);
        int emailIndex = emailHash % this.hashTable.length;

        // check if user in table already
        for (int i = 0; i < this.hashTable.length; i++) {
            int probeLocation = (i + emailIndex) % this.hashTable.length;
            UserInfo userInfo = this.hashTable[i];

            // Check if user already in system
            if (userInfo.getEmail().equals(email)) {
                return
            }
        }



        //
        this.numUsers++;
        return false;
    }

    /** TODO pass function callback into linearprobe that will handle what to do if you find the
     * user already in the hashtable e.g. removeuser, addUser, changePassword will all do
     * linearprobing but will have different functionality */
//    public boolean linearProbe(int emailIndex, UserInfo user) {
//        for (int i = 0; i < this.hashTable.length; i++) {
//            int probeLocation = (i + emailIndex) % this.hashTable.length;
//
//            // The spot is empty
//            if (this.hashTable[probeLocation] != null) { continue; }
//            this.hashTable[probeLocation] = user;
//            return true;
//        }
//        return false;
//    }

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
            int emailHash = this.hashCode(user.getEmail());
            int emailIndex = emailHash % this.hashTable.length;

            // Insert email into new hash table
            for (int j = 0; j < this.hashTable.length; j++) {
                int probeLocation = (j + emailIndex) % this.hashTable.length;
                UserInfo userNewTable = this.hashTable[probeLocation];

                // If we find empty spot put user in
                if (userNewTable == null) {
                    this.hashTable[probeLocation] = userOldTable;
                }
            }
        }
    }

    @Override
    public boolean removeUser(String email, String password) {
        /* Add your code here! */
        int hashedEmail = this.hashCode(email);
        int emailIndex = hashedEmail % this.hashTable.length;
        for (int i = 0; i < this.hashTable.length; i++) {
            int probeLocation = (i + emailIndex) % this.hashTable.length;
            UserInfo userInfo = this.hashTable[probeLocation];

            // For each user in the hashtable check the provided email matches the plain text email
            // and hashed password in the hashtable
            if (userInfo.getEmail().equals(email)
                && userInfo.getPasswordHash() == this.hashCode(password)) {
                this.hashTable[probeLocation] = null; // remove user
                return true;
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

class UserInfo {

    /** Plaintext email stored for user otherwise if only a hashed email was stored you could end
     * up with users with the same email hash and password hash */
    private String email;

    /** Password hash stored for user */
    private int passwordHash;

    public UserInfo(String email, int passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
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
}
