package com.weareadaptive.auction.model.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
  @Id
  @SequenceGenerator(
      name = "organization_sequence",
      sequenceName = "organization_sequence",
      allocationSize = 1
  )
  @GeneratedValue(
      strategy = GenerationType.IDENTITY,
      generator = "organization_sequence"
  )
  private int orgId;

  @Column (
      nullable = false,
      name = "organization_name"
  )
  private String organisationName;
}
