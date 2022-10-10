package com.database.database.exeption;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CheckedExceptionTest {


    @Test
    void checkedError_catch(){
        ExceptionCatchExample exceptionCatchExample = new ExceptionCatchExample();
        exceptionCatchExample.errorCatch();
    }
    @Test
    void checkedError_throw() throws CheckedTest.CheckedExceptionExample {
        ExceptionThrowExample exceptionThrowExample = new ExceptionThrowExample();
        Assertions.assertThatThrownBy(()-> exceptionThrowExample.errorThrow())
                .                isInstanceOf(CheckedTest.CheckedExceptionExample.class);
    }

    static class CheckedExceptionExample extends Exception{
        public CheckedExceptionExample(String message){
            super(message);
        }
    }

    static class ExceptionCatchExample{
        CheckedTest.ExceptionThrowExample repository = new CheckedTest.ExceptionThrowExample();

        public void errorCatch(){
            try {
                repository.errorThrow();
//            } catch (CheckedTest.CheckedExceptionExample e) {
            } catch (Exception e) {
                log.info("this is error message = {}", e.getMessage(), e);
            }
        }
    }

    static class ExceptionThrowExample{
        public void errorThrow() throws CheckedTest.CheckedExceptionExample {
            throw new CheckedTest.CheckedExceptionExample("error message");
        }
    }


}