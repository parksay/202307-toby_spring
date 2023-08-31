package basics.tobyspring6.chapter63;

public class Message634 {

    String text;

    private Message634(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static Message634 newMessage(String text) {
        return new Message634(text);
    }

}


// p.450 - chapter 6.3.4
//Message634 라는 클래스도 보면 프록시랑 비슷하지.
//기본 생성자는 private 으로 선언해서 외부 접근을 막아 놨어.
//인스턴스를 얻는 방법은 스태틱 팩토리 메소드를 호출해서 하나씩 받아오는 수밖에 없어.
//거기에 파라미터 오고 가는 것도 이해하려고 text 라는 클래스 필드도 하나 추가해 놨고.
//필드는 또 생성자로 받아야 함.
//근데 이제 이 Message634 라는 클래스를 빈으로 등록하고 다른 클래스들은 스프링 컨테이너한테서 빈을 받아다 써야 하는 상황이야.
//이렇게 기본 생성자가 막혀 있고 스태틱 팩토리 메소드로만 접근할 수 있는 애들은 어떻게 스프링 컨테이너에 등록할 수가 있느냐?
//바로 FactoryBean 이라는 인터페이스를 구현하는 것.
//FactoryBean 인터페이스 구현해서 만든 클래스가 MessageFactoryBean634.class 인데 그리로 가보자.



