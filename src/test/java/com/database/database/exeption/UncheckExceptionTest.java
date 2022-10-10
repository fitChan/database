package com.database.database.exeption;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UncheckExceptionTest {

    @Test
    void catchUnchecked() {
            CatchRuntimeExeption catchException = new CatchRuntimeExeption();
            catchException.catchException();
    }
    @Test
    void ThrowUnchecked() {
        CatchRuntimeExeption catchException = new CatchRuntimeExeption();

        Assertions.assertThatThrownBy(catchException::throwException)
                .isInstanceOf(RuntimeExceptionExtends.class);
    }


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
