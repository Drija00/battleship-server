package main.java.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Player implements GenericEntity{
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private int wins;

    public Player(Long id, String firstname, String lastname, String username, String password, int wins) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.wins =wins;
    }

    public Player(String name) {
        this.firstname = name;
    }

    public Player() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Player player = (Player) object;
        return wins == player.wins && Objects.equals(id, player.id) && Objects.equals(firstname, player.firstname) && Objects.equals(lastname, player.lastname) && Objects.equals(username, player.username) && Objects.equals(password, player.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, username, password, wins);
    }

    @Override
    public String getClassName() {
        return "player";
    }

    @Override
    public String getAtrValues() {
        StringBuilder sb = new StringBuilder();
        sb.append("'").
                append(firstname).
                append("','").
                append(lastname).
                append("','").
                append(username).
                append("','").
                append(password).
                append("',").
                append(wins).
                append("");
        return sb.toString();
    }

    @Override
    public String getAtrNames() {
        return "firstname,lastname,username,password,wins";
    }

    @Override
    public String setAtrValues() {
            return "firstname='"+getFirstname()+"',"+
                    "lastname='"+getLastname()+"',"+
                    "username='"+getUsername()+"',"+
                    "password='"+getPassword()+"',"+
                    "wins="+getWins();
    }

    @Override
    public String getWhereCondition() {
        return "username='" + username + "'"+ "AND password='" + password + "'";
    }

    @Override
    public GenericEntity getNewRecord(ResultSet rs) {
        try {
            long dbId = rs.getLong("id");
            String dbfirstname = rs.getString("firstname");
            String dblastname = rs.getString("lastname");
            String dbusername = rs.getString("username");
            String dbPassword = rs.getString("password");
            int dbWins = rs.getInt("wins");

            return new Player(dbId, dbfirstname,dblastname,dbusername, dbPassword,dbWins);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", wins=" + wins +
                '}';
    }
}
