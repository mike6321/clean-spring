package tobyspring.splearn.domain.member;

public class DuplicationEmailException extends RuntimeException{
    public DuplicationEmailException(String message) {
        super(message);
    }

}
