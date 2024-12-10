package model;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class User implements Serializable {
    private final String username;
    private final String passwordHash;
    private final byte[] salt;
    private final Wallet wallet;

    public User(String username, String password) {
        this.username = username;
        this.salt = generateSalt(); // Генерируем случайную "соль"
        this.passwordHash = hashPassword(password, this.salt); // Вычисляем хэш пароля
        this.wallet = new Wallet();
    }

    public String getUsername() {
        return username;
    }

    public boolean validatePassword(String password) {
        String hashedInput = hashPassword(password, this.salt);
        return this.passwordHash.equals(hashedInput);
    }

    public Wallet getWallet() {
        return wallet;
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[16];
        new java.security.SecureRandom().nextBytes(salt);
        return salt;
    }

    private String hashPassword(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Ошибка хэширования пароля", e);
        }
    }
}
