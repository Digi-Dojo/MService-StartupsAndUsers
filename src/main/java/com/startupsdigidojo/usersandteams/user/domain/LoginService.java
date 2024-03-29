package com.startupsdigidojo.usersandteams.user.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
public class LoginService {

    private final UserBroadcaster userBroadcaster;

    @Autowired
    public LoginService(UserBroadcaster userBroadcaster) {
        this.userBroadcaster = userBroadcaster;
    }

    /**
     * The password and mail address of the user are put together in a string before being hashed,
     * so we avoid having multiple users with the same hash because they chose the same password
     *
     * @param password    the password we want to hash
     * @param mailAddress mail address of the user, included in the hashing of the password
     * @return the string containing the hashed password
     * @throws RuntimeException if no Provider supports a MessageDigestSpi implementation for the specified algorithm
     */
    public String hashPassword(String password, String mailAddress) {
        try {
            password = mailAddress + password;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a string of double the size of the bytes array given as input, containing the hexadecimal
     * values of all the bytes in the array.<br>
     * This method traverses the array translating every element in hex and appends it to a StringBuilder;
     * because the string is double the size of the array, for values that translate to a single digit hex,
     * a 0 is appended before the translated value
     *
     * @param bytes array of bytes generated by the digest of the password
     * @return a string of double the size of the bytes array, containing the hexadecimal values of all
     * the bytes in the array
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


    /**
     * @param user            the user, for whom we need to check the password
     * @param enteredPassword the password entered by the person trying to log in
     * @return the user
     * @throws IllegalArgumentException if the entered password isn't the one that belongs to the user
     */
    public User verifyPassword(User user, String enteredPassword) {
        String hashedEnteredPassword = hashPassword(enteredPassword, user.getMailAddress());
        if (!hashedEnteredPassword.equals(user.getPassword())) {
            throw new IllegalArgumentException("Wrong password for this user");
        }
        userBroadcaster.emitUserLogIn(user);
        return user;
    }
}
