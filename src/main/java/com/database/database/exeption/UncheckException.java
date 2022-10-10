package com.database.database.exeption;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UncheckException {

    static class RuntimeExceptionExtends extends RuntimeException {
        public RuntimeExceptionExtends(String message) {
            super(message);
        }
    }

    static class ThrowRuntimeExeption {
        public void makeException() {
            throw new RuntimeExceptionExtends("throw Exception");
        }
    }

    static class CatchRuntimeExeption {
        ThrowRuntimeExeption throwRuntimeExeption = new ThrowRuntimeExeption();

        public void catchException() {
            try {
                throwRuntimeExeption.makeException();
            } catch (RuntimeExceptionExtends e) {
                log.error("error message = {}", e.getMessage(), e);
            }
        }

        public void throwException(){
            throwRuntimeExeption.makeException();
        }

    }

}
