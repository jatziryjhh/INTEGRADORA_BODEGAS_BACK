package mx.edu.utez.Backend.Bodegas.security.dto;

public class LoginResponseDto {
    private String token;
    private String rol;
    private int id;

    public LoginResponseDto(String token, String rol, int id) {
        this.token = token;
        this.rol = rol;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public String getRol() {
        return rol;
    }

    public int getId() {
        return id;
    }
}
