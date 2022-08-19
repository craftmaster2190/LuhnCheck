package com.craftmaster.luhncheck;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class LuhnCheckApplication {
  public static void main(String[] args) {
    SpringApplication.run(LuhnCheckApplication.class, args);
  }
}

@Slf4j
@Component
class Command$ implements ApplicationRunner {

  private static int sumDigits(int number) {
    int sum;
    for (sum = 0; number > 0; sum = sum + number % 10, number = number / 10)
      ;
    return sum;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    val cardNumber = args.getNonOptionArgs().get(0);
    val luhnNumbers = cardNumber.substring(0, cardNumber.length() - 1);
    val checkDigit = cardNumber.substring(cardNumber.length() - 1);

    boolean isDoubled = true;
    int checkSum = 0;
    for (char digit : luhnNumbers.toCharArray()) {
      int digitAsInt = Character.getNumericValue(digit);
      int digitDoubledIfNeeded = isDoubled ? (digitAsInt * 2) : digitAsInt;
      int sumOfDigitsOfDoubleDigit = sumDigits(digitDoubledIfNeeded);
      checkSum += sumOfDigitsOfDoubleDigit;

      log.info("digit={} digitDoubledIfNeeded={} sumOfDigitsOfDoubleDigit={}", digit, digitDoubledIfNeeded, sumOfDigitsOfDoubleDigit);

      isDoubled = !isDoubled;
    }

    int luhnNumber = 10 - (checkSum % 10);
    log.info("checkSum={} luhnFormula=(10 - ({} % 10)) => luhnNumber={} {} checkDigit={}", checkSum, checkSum, luhnNumber,
        luhnNumber == Integer.valueOf(checkDigit) ? "==" : "!=", checkDigit);
  }
}
