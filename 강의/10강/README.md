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
package org.springframework.core.convert.converter;

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

## 타입 컨버터 - Converter

타입 컨버터를 사용하려면 `org.springframework.core.convert.converter.Converter` 인터페이스를 구현하면 된다.

> **주의**<br>
> `Converter`라는 이름의 인터페이스가 많으니 조심해야 한다.
> `org.springframework.core.convert.converter.Converter`를 사용해야 한다.

### 예제 1

#### StringToIntegerConverter

```java
@Slf4j
public class StringToIntegerConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String source) {
        log.info("Convert String To Integer source = {}", source);
        return Integer.valueOf(source);
    }
}
```

#### IntegerToStringConverter

```java
@Slf4j
public class IntegerToStringConverter implements Converter<Integer, String> {
    @Override
    public String convert(Integer source) {
        log.info("Convert Integer To String source = {}", source);
        return String.valueOf(source);
    }
}
```

#### ConverterTest

```java
public class ConverterTest {
    @Test
    void stringToInteger() {
        StringToIntegerConverter converter = new StringToIntegerConverter();
        Integer result = converter.convert("10");
        assertThat(result).isEqualTo(10);
    }

    @Test
    void integerToString() {
        IntegerToStringConverter converter = new IntegerToStringConverter();
        String result = converter.convert(10);
        assertThat(result).isEqualTo("10");
    }
}
```

#### 결과

```
Convert String To Integer source = 10
Convert Integer To String source = 10
```

### 예제 2

#### IpPort

```java
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class IpPort {
    private final String ip;
    private final int port;
}
```

#### StringToIpPortConverter

```java
@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {
    @Override
    public IpPort convert(String source) {
        log.info("Convert String To IpPort source = {}", source);

        String[] split = source.split(":");
        return new IpPort(
                split[0],
                Integer.parseInt(split[1])
        );
    }
}
```

#### IpPortToStringConverter

```java
@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String> {
    @Override
    public String convert(IpPort source) {
        log.info("Convert IpPort To String source = {}", source);
        return source.getIp() + ":" + source.getPort();
    }
}
```

#### ConverterTest

```java
public class ConverterTest {
    @Test
    void stringToIpPort() {
        StringToIpPortConverter converter = new StringToIpPortConverter();
        IpPort result = converter.convert("127.0.0.1:8080");
        assertThat(result).isEqualTo(new IpPort("127.0.0.1", 8080));
    }

    @Test
    void ipPortToString() {
        IpPortToStringConverter converter = new IpPortToStringConverter();
        String result = converter.convert(new IpPort("127.0.0.1", 8080));
        assertThat(result).isEqualTo("127.0.0.1:8080");
    }
}
```

#### 결과

```
Convert String To IpPort source = 127.0.0.1:8080
Convert IpPort To String source = hello.springcoremvc210.type.IpPort@59cb0946
```

### 정리

타입 컨버터 인터페이스가 단순해서 이해하기 어렵지 않을 것이다.
그런데 이렇게 타입 컨버터를 하나하나 직접 사용하면, 개발자가 직접 컨버팅 하는 것과 큰 차이가 없다.
**타입 컨버터를 등록하고 관리하면서 편리하게 변환 기능을 제공하는 역할을 하는 무언가**가 필요하다.

### 참고

> 참고<br>
> 스프링은 용도에 따라 다양한 방식의 타입 컨버터를 제공한다.
>
> * `Converter`: 기본 타입 컨버터
> * `ConverterFactory`: 전체 클래스 계층 구조가 필요할 때
> * `GenericConverter`: 정교한 구현, 대상 필드의 애노테이션 정보 사용 가능
> * `ConditionalGenericConverter`: 특정 조건이 참인 경우에만 실행
>
> 자세한 내용은 공식 문서를 참고하자.
> * https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#coreconvert

> 참고<br>
> 스프링은 문자, 숫자, 불린, Enum 등 일반적인 타입에 대한 대부분의 컨버터를 기본으로 제공한다.
> IDE에서 `Converter`, `ConverterFactory`, `GenericConverter`의 구현체를 찾아보면 수 많은 컨버터를 확인할 수 있다.

## 컨버전 서비스 - ConversionService

이렇게 타입 컨버터를 하나하나 직접 찾아서 타입 변환에 사용하는 것은 매우 불편하다.
그래서 스프링은 개별 컨버터를 모아두고 그것들을 묶어서 편리하게 사용할 수 있는 기능을 제공하는데,
이것이 바로 컨버전 서비스( `ConversionService` )이다.

### ConversionService

```java
package org.springframework.core.convert;

public interface ConversionService {

    // 컨버팅 가능한지 여부
	boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType);
	boolean canConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType);

    // 컨버팅 기능
	@Nullable
	<T> T convert(@Nullable Object source, Class<T> targetType);

	@Nullable
	Object convert(@Nullable Object source, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType);
}
```

### 예제

#### ConversionServiceTest

```java
public class ConversionServiceTest {

    @Test
    void conversionService() {
        // 등록
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        // 사용
        assertThat(conversionService.convert("10", Integer.class)).isEqualTo(10);
        assertThat(conversionService.convert(10, String.class)).isEqualTo("10");

        IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));

        String ipPortString = conversionService.convert(new IpPort("127.0.0.1", 8080), String.class);
        assertThat(ipPortString).isEqualTo("127.0.0.1:8080");
    }
}
```

#### 결과

```java
[main] INFO hello.springcoremvc210.converter.StringToIntegerConverter - Convert String To Integer source = 10
[main] INFO hello.springcoremvc210.converter.IntegerToStringConverter - Convert Integer To String source = 10
[main] INFO hello.springcoremvc210.converter.StringToIpPortConverter - Convert String To IpPort source = 127.0.0.1:8080
[main] INFO hello.springcoremvc210.converter.IpPortToStringConverter - Convert IpPort To String source = hello.springcoremvc210.type.IpPort@59cb0946
```

### 정리

#### 등록과 사용 분리

컨버터를 등록할 때는 `StringToIntegerConverter`같은 타입 컨버터를 명확하게 알아야 한다.
반면에 컨버터를 사용하는 입장에서는 타입 컨버터를 전혀 몰라도 된다.
타입 컨버터들은 모두 컨버전 서비스 내부에 숨어서 제공된다.
따라서 타입을 변환을 원하는 사용자는 컨버전 서비스 인터페이스에만 의존하면 된다.
물론 컨버전 서비스를 등록하는 부분과 사용하는 부분을 분리하고 의존관계 주입을 사용해야 한다.

#### 컨버전 서비스 사용

```java
Integer value = conversionService.convert("10", Integer.class)
```

### 인터페이스 분리 원칙 - ISP(Interface Segregation Principle)

`DefaultConversionService`는 다음 두 인터페이스를 구현했다.

* `ConversionService`: 컨버터 **사용**에 초점
* `ConverterRegistry`: 컨버터 **등록**에 초점

이렇게 인터페이스를 분리하면 컨버터를 사용하는 클라이언트와 컨버터를 등록하고 관리하는 클라이언트의 관심사를 명확하게 분리할 수 있다.
특히 컨버터를 사용하는 클라이언트는 `ConversionService`만 의존하면 되므로, 컨버터를 어떻게 등록하고 관리하는지는 전혀 몰라도 된다.
결과적으로 컨버터를 사용하는 클라이언트는 꼭 필요한 메서드만 알게된다.
이렇게 인터페이스를 분리하는 것을 ISP 라 한다.

스프링은 내부에서 `ConversionService`를 사용해서 타입을 변환한다.
예를 들어서 앞서 살펴본 `@RequestParam`같은 곳에서 이 기능을 사용해서 타입을 변환한다.

이제 컨버전 서비스를 스프링에 적용해보자.

## 스프링에 Converter 적용하기

## 뷰 템플릿에 Converter 적용하기

## 포맷터 - Formatter

## 포맷터를 지원하는 컨버전 서비스

## 포맷터 적용하기

## 스프링이 제공하는 기본 포맷터
