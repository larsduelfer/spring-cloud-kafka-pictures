package info.novatec.spring.showcase.model;

import lombok.Getter;

public enum ImageResolution {

  /** 6 Mega pixel. */
  MP_6(6000000L),

  /** 1 Mega pixel. */
  MP_1(1000000L),

  /** Raw. */
  RAW(0L);

  @Getter private final Long maxResolution;

  ImageResolution(Long maxResolution) {
    this.maxResolution = maxResolution;
  }
}
