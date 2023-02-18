# 스프링 타입 컨버터

## 프로젝트 생성

* 프로젝트 선택
    * Project: Gradle Project
    * Language: Java
    * Spring Boot: 3.0.2
* Project Metadata
    * Group: hello
    * Artifact: spring-core-mvc2-10
    * Name: spring-core-mvc2-10
    * Package name: hello.spring-core-mvc2-10
    * Packaging: Jar
    * Java: 17
    * Dependencies: Spring Web, Lombok, Thymeleaf

## 스프링 타입 컨버터 소개

문자를 숫자로 변환하거나, 반대로 숫자를 문자로 변환해야 하는 것 처럼
애플리케이션을 개발하다 보면 타입을 변환해야 하는 경우가 상당히 많다.

### 예제 1

#### HelloController

```java
@Slf4j
@RestController
public class HelloController {
    @GetMapping("/hello/v1")
    public String helloV1(
            HttpServletRequest req
    ) {
        String data = req.getParameter("data");
        Integer intValue = Integer.valueOf(data);

        log.info("intValue = {}", intValue);
        log.info("typeof data = {}", data.getClass().getSimpleName());
        log.info("typeof intValue = {}", intValue.getClass().getSimpleName());

        return "hello " + data;
    }
}
```

#### 결과

```
intValue = 10
typeof data = String
typeof intValue = Integer
```

### 예제 2

#### HelloController

```java
@Slf4j
@RestController
public class HelloController {
    @GetMapping("/hello/v2")
    public String helloV2(
            @RequestParam Integer data
    ) {
        log.info("data = {}", data);
        log.info("typeof data = {}", data.getClass().getSimpleName());

        return "hello " + data;
    }
}
```

#### 결과

```
data = 10
typeof data = Integer
```

### 설명

앞서 보았듯이 HTTP 쿼리 스트링으로 전달하는 data=10 부분에서 10은 숫자 10이 아니라 문자 10이다.
스프링이 제공하는 `@RequestParam`을 사용하면 이 문자 10을 Integer 타입의 숫자 10으로 편리하게 받을 수 있다.

이것은 스프링이 중간에서 타입을 변환해주었기 때문이다.

이러한 예는 `@ModelAttribute`, `@PathVariable`에서도 확인할 수 있다.

#### 스프링의 타입 변환 적용 예

* 스프링 MVC 요청 파라미터
    * @RequestParam , @ModelAttribute , @PathVariable
* @Value 등으로 YML 정보 읽기
* XML에 넣은 스프링 빈 정보를 변환
* 뷰를 렌더링 할 때

### 스프링과 타입 변환

이렇게 타입을 변환해야 하는 경우는 상당히 많다.
개발자가 직접 하나하나 타입 변환을 해야 한다면, 생각만 해도 괴로울 것이다.
스프링이 중간에 타입 변환기를 사용해서 타입을 String Integer 로 변환해주었기 때문에 개발자는 편리하게 해당 타입을 바로 받을 수 있다.
앞에서는 문자를 숫자로 변경하는 예시를 들었지만, 반대로 숫자를 문자로 변경하는 것도 가능하고, Boolean 타입을 숫자로 변경하는 것도 가능하다.
만약 개발자가 새로운 타입을 만들어서 변환하고 싶으면 어떻게 하면 될까?

### 컨버터 인터페이스

```java
@FunctionalInterface
public interface Converter<S, T> {
	@Nullable
	T convert(S source);
}
```

스프링은 확장 가능한 컨버터 인터페이스를 제공한다.

개발자는 스프링에 추가적인 타입 변환이 필요하면 이 컨버터 인터페이스를 구현해서 등록하면 된다.
이 컨버터 인터페이스는 모든 타입에 적용할 수 있다.
필요하면 X -> Y 타입으로 변환하는 컨버터 인터페이스를 만들고, 또 Y -> X 타입으로 변환하는 컨버터 인터페이스를 만들어서 등록하면 된다.

예를 들어서 문자로 "true" 가 오면 Boolean 타입으로 받고 싶으면 String -> Boolean 타입으로 변환되도록 컨버터 인터페이스를 만들어서 등록하고,
반대로 적용하고 싶으면 Boolean -> String 타입으로 변환되도록 컨버터를 추가로 만들어서 등록하면 된다.

> **참고**<br>
> 과거에는 `PropertyEditor`라는 것으로 타입을 변환했다.
> `PropertyEditor`는 동시성 문제가 있어서 타입을 변환할 때 마다 객체를 계속 생성해야 하는 단점이 있다.
> 지금은 `Converter`의 등장으로 해당 문제들이 해결되었고, 기능 확장이 필요하면 `Converter`를 사용하면 된다

## 타입 컨버터 - Convertor

## 컨버전 서비스 - ConversionService

## 스프링에 Convertor 적용하기

## 뷰 템플릿에 Convertor 적용하기

## 포맷터 - Formatter

## 포맷터를 지원하는 컨버전 서비스

## 포맷터 적용하기

## 스프링이 제공하는 기본 포맷터
