package basics.tobyspring5.chapter51;

import java.util.List;

public class UserService512 {

    private UserDao512 userDao;
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;


    public void setUserDao(UserDao512 userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevel(User512 user) {
        user.upgradeLevel();
        this.userDao.update(user);
    }

    public void upgradeLevels() {
        //
        List<User512> userList = userDao.getAll();
        //
        for(User512 user : userList) {
            if(this.canUpgradeLevel(user)) {
                this.upgradeLevel(user);
            }
        }
    }

    public boolean canUpgradeLevel(User512 user) {
        Level512 currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= 50);
            case SILVER: return (user.getRecommend() >= 30);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    public void add(User512 user) {
        if(user.getLevel() == null) {
            user.setLevel(Level512.BASIC);
        }
        this.userDao.add(user);
    }
}

//private void upgradeLevel(User512 user) {
//    if(user.getLevel() == Level512.BASIC) {
//        user.setLevel(Level512.SILVER);
//    } else if(user.getLevel() == Level512.SILVER) {
//        user.setLevel(Level512.GOLD);
//    }
//    this.userDao.update(user);
//}

//UserService511.upgradeLevels(); 보자.
//사실 그 자체로 완성됐다고 생각할 수 있어.
//지금 당장 돌리기에는 문제 없기도 하고.
//어느 정도, '레벨을 업그레이드하는 기능'이라는 걸로 응집도 높게 묶여 있는 것 같지.
//얼핏 봐서는 그래.
//근데 그게 뜯어봐도 그럴까?
//코드를 보면서 하는 기능들을 분리해 보자.
//첫째, user level을 업그레이드할 수 있는지 확인해.
//둘째, 업그레이드 할 수 있다면 level 을 다음 단계로 set 해줘.
//셋째, 마지막으로 UserDao.update() 까지 호출해 줘.
//이 세 가지 기능을 혼자서 다 진행하고 있음.
//딱 봤을 때 무슨 기능을 하는 건지 한눈에 보기 어렵고 자기 책임이 아닌 일까지 다 떠맡아서 수행하고 있음.
//첫째 ,업그레이드 할 수 있는지 확인하는 기능은 메소드를 따로 빼자고.
//둘째, 레벨 다음 단계는 레벨 이늄에게 넘기자고.
//셋째, UserDao.upate 는 Level 업그레이드 해주고 user 저장해주기.
//첫째로, 업그레이드 가능한지 확인하는 기능은 얼핏 보기엔 문제 없어 보였지.
//근데 이게 변화에 되게 취약해.
//이렇게 if 문으로 범벅한 코드는 지금 상황에 딱 끼워맞춰서 돌아가는 거야.
//만약 레벨 업그레이드 조건이 바뀌면...?
//하다못해 레벨이 하나가 더 생기거나, 조건이 마지막 로그인 날짜 같은 게 또 생기면?
//이 레벨에서는 로그인 횟수가 이 이상 돼야 하고, 추천은 이만큼 받아야 하고, 대신 마지막 로그인은 이 날짜 아래여야 하고,
//저 레벨에서는 마지막 로그인은 상관은 없는데, 로그인 횟수랑 추천이 최소 얼마가 돼야 하고...
//조건이 조금만 복잡해지거나 레벨이 좀 더 다양해지기 시작하면 if 문을 다중으로 여러 겹 겹쳐서 쓸 수밖에 없게 되고 지저분해짐.
//조건이나 기준이 추가되면 경우의 수가 더하기로 늘어나는 게 아니라 곱하기로 늘어남.(A이면서 B는 아니면서 C이면서, A이면서 B이면서 C는 아니면서...)
//사실 알고 보니 변경에 굉장히 취약한 코드지.
//따라서 업그레이드 가능한지 판단하는 기능은 메소드를 따로 뽑아야 해.
//따로 메소드로 뽑았고, switch 문으로 구성해서 기대하는 값이 아니라면 throw 예외 처리까지 해줄 수 있게끔 했어.
//둘째 레벨 다음 단계는 이늄에게 넘기자고.
//다음 단계 레벨을 비지니스 로직에서 내가 정해주는 게 맞아..?
//SILVER 업그레이드하면 GOLD 되는 거 지금은 그렇지만 변경되면...?
//다음 Level 관련된 코드 다 일일이 찾아서 수정해줘야 해...?
//응집도나 책임을 봤을 때 비지니스 로직 계층에서 맡아서 하는 게 맞아?
//BASIC 다음 레벨은 SILVER 야, SILVER 다음 레벨은 GOLD 야, 서비스 계층이 비지니스 로직이 정해주는 게 맞아?
//어떻게 보면 이건 일일이 찾아가서 하드코딩으로 지정해주고 있는 느낌이지.
//이 Level에서 다음 Level이 뭔지 결정하는 건 Level 클래스로 묶여 있는, Level 이늄에 모아 놓는 게 맞는 거 같아.
//"지금 Level이 이거인데 그 다음 Level을 주세요!" 라고 Level 이늄에다가 요청을 하는 게 맞는 거 같아.
//그래서 Level 이늄에다가 nextLevel() 을 만들어서 받아오게끔 수정했어.
//셋째, UserDao.upate 는 Level 업그레이드 해주고 user 저장해주기.
//업데이트만 해달라고 하는 거라서 업데이트 할지 말지, 어떤 걸로 업데이트 할지, 이런 거는 다 상위 메소드에서 판단해주고 해달라고 할 때만 해주면 됨.
//user 에다가 Level 을 직접 set 해주는 게 아니라, user 보고 요청하기.
//user 님아 님 현재 레벨 올려주세요!
//라고.
//UserService 계층에서 직접 user.setLevel(~~) 하는 게 아니라
//user.upgradeLevel() 이렇게 부르기.
//user 안에 자신의 레벨을 업그레이드 하는 메소드를 하나 만들었고, 로직이 추가됐으니 UserTest 도 따로 만들었음.
//
// 'SILVER는 로그인 횟수가 30 이상일 때 업그레이드'
// 여기서 30
// 이런 값들도 다 상수로 지정해두고 여기 저기서 가져다 쓰기
// 중복이기도 하고, 수정이라도 되는 날에는 다같이 변경이 일어나야 하고, 하드코딩으로 숫자 직접 넣다 보면 오타나 실수 나기 마련이고.
