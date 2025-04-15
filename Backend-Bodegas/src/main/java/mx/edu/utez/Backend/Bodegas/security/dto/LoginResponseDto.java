package mx.edu.utez.Backend.Bodegas.security.dto;

public class LoginResponseDto {
    private String token;
    private String rol;
    private int id;

    private boolean status;

    public LoginResponseDto(String token, String rol, int id, boolean status) {
        this.token = token;
        this.rol = rol;
        this.id = id;
        this.status=status;
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

    public void setToken(String token) {
        this.token = token;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
