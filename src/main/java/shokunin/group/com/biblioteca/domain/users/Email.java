package shokunin.group.com.biblioteca.domain.users;

public final class Email {
    private final String email;
    private static final String EMAIL_PATTERN = "^[\\w.-]+@[\\w.-]+\\.[a-z]{2,3}$";

    private Email(String email) {

        if(email==null || email.isBlank() || !email.matches(EMAIL_PATTERN)){
            throw new IllegalArgumentException("Email é obrigatório");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return email;
    }

    public static Email of(String email){
        return new Email(email);
    }

    @Override
    public boolean equals(Object o){
        if(this ==o)return true;
        if(o==null || getClass() != o.getClass()) return false;
        Email email = (Email) o;

        return java.util.Objects.equals(this.email, email.email);

    }
    @Override
    public int hashCode(){
        return java.util.Objects.hash(email);
    }


}
