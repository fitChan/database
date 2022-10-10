package com.database.database.exeption;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckedTest {

    static class CheckedExceptionExample extends Exception{
        public CheckedExceptionExample(String message){
            super(message);
        }
    }

    static class ExceptionCatchExample{
        ExceptionThrowExample repository = new ExceptionThrowExample();

        public void errorCatch(){
            try {
                repository.errorThrow();
            } catch (CheckedExceptionExample e) {
                log.info("this is error message = {}", e.getMessage(), e);
            }
        }
    }

    static class ExceptionThrowExample{
        public void errorThrow() throws CheckedExceptionExample {
            throw new CheckedExceptionExample("error message");
        }
    }


}
