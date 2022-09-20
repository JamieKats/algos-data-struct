

public class LoginSystem extends LoginSystemBase {

    private final int initialSize = 101;

    UserInfo[] hashTable;

    private int numUsers;

    private final int hashConstant = 31;

    private final double loadFactor = 0.75;

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
        return (this.hashTable.length * this.loadFactor <= this.getNumUsers());
    }

    public int linearProbe(UserInfo user) {
        int emailHash = this.hashCode(user.getEmail());
        int emailIndex = emailHash % hashTable.length;

        int deletedUserIndex = -1; // used to keep track of the first sentiel values passed
        for (int i = 0; i < this.hashTable.length; i++) {
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
        int oldHashTableSize = this.hashTable.length;
        int newHashTableSize = oldHashTableSize * 3;
        UserInfo[] oldHashTable = this.hashTable;
        this.hashTable = new UserInfo[newHashTableSize];

        // for each user in the old hash table rehash and compress the email and put it into the new
        // hashtable
        for (int i = 0; i < oldHashTable.length; i++) {
            // skip over empty spaces and users in old hash table that are deleted
            if (oldHashTable[i] == null || oldHashTable[i].getIsDeleted()) {
                continue;
            }
            // spaces
            UserInfo userOldTable = oldHashTable[i];

            // Rehash and compress email with new array size
            int emailHash = this.hashCode(userOldTable.getEmail());
            int emailIndex = emailHash % this.hashTable.length;

            // Insert email into new hash table by linear probing
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

        // linear probe until you find the user you want to remove
        for (int i = 0; i < this.hashTable.length; i++) {
            int probeLocation = (i + emailIndex) % this.hashTable.length;
            UserInfo probedUser = this.hashTable[probeLocation];

            // Finding a null means the user isnt in the hash table
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
        return false; // user not found in hash table
    }



    @Override
    public int checkPassword(String email, String password) {
        /* Add your code here! */
        UserInfo user = new UserInfo(email, this.hashCode(password));
        int hashedEmail = this.hashCode(email);
        int emailIndex = hashedEmail % this.hashTable.length;

        // linear probe
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

        // linear probe until you find the user you are looking for
        for (int i = 0; i < this.hashTable.length; i++) {
            int probeLocation = (i + emailIndex) % this.hashTable.length;
            UserInfo probedUser = this.hashTable[probeLocation];

            if (probedUser == null) { // user could not be found
                break;
            }

            // If user in system and old password == password in system
            if (probedUser.equals(oldUser)) {
                // change password
                this.hashTable[probeLocation].setPasswordHash(this.hashCode(newPassword));
                return true;
            } else if (probedUser.sameUserWrongPassword(oldUser)) { // old password incorrect
                break;
            }
        }
        // user could not be found or user found but password was incorrect
        return false;
    }

    /* Add any extra functions below */
}

class UserInfo {
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
