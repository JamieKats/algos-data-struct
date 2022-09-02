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
        return hashCode(key.substring(0, key.length() - 1)) * hashConstant + key.charAt(key.length() - 1);
    }

//    public int hashHelper(String key) {
//         if (key.length() == 1) {
//             return key.charAt(0);
//         }
//         return hashHelper(key.substring(0, key.length() - 1)) * hashConstant + key.charAt(key.length() - 1);
//    }

    @Override
    public boolean addUser(String email, String password) {
        /* Add your code here! */
        // Check if hash table needs to be resized
        if (this.numUsers == hashTable.length) {
            growArrayTripleStrategy();
        }
        UserInfo userInfo = new UserInfo(email, hashCode(password));

        // Calculate hash of email and password
        int emailHash = hashCode(email);
        int passwordHash = hashCode(password);
        int userInfoIndex = emailHash % this.hashTable.length;

        // store (email, hashed(password)) in array at index of compressed(hashed(email))
        linearProbe(userInfoIndex, )
        //

        //
        return false;
    }

    /** TODO pass function callback into linearprobe that will handle what to do if you find the
     * user already in the hashtable e.g. removeuser, addUser, changePassword will all do
     * linearprobing but will have different functionality */
    public boolean linearProbe(int emailIndex, UserInfo user) {
        for (int i = 0; i < this.hashTable.length; i++) {
            int probeLocation = (i + emailIndex) % this.hashTable.length;
            if (this.hashTable[probeLocation] != null) { continue; }
            this.hashTable[probeLocation] = user;
            return true;
        }
        return false;
    }

    public void growArrayTripleStrategy() {
        int oldHashTableSize = this.hashTable.length;
        int newHashTableSize = oldHashTableSize * 3;
        UserInfo[] oldHashTable = this.hashTable;
        this.hashTable = new UserInfo[newHashTableSize];

        // for each user in the hash table rehash and compress the email and put it into the new
        // hashtable
        for (int i = 0; i < oldHashTableSize; i++) {
            if (oldHashTable[i] == null) { continue; }
            UserInfo user = oldHashTable[i];

            // Rehash and compress email with new array size
            // allowing this code since emails will be limited in size Ed Post #207
            int emailHash = hashCode(user.getEmail());
            int emailIndex = emailHash % newHashTableSize;

            // Insert email into new hash table
            linearProbe(emailIndex, user);
        }
    }

    @Override
    public boolean removeUser(String email, String password) {
        /* Add your code here! */
        return false;
    }

    @Override
    public int checkPassword(String email, String password) {
        /* Add your code here! */
        return 0;
    }

    @Override
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        /* Add your code here! */
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

    private String email;
//    private int hashedEmail;
    private int password;

    public UserInfo(String email, int password) {
        this.email = email;
        this.password = password;

//        this.hashedEmail = hashCode(email);
    }

    public String getEmail() {
        return this.email;
    }
}
