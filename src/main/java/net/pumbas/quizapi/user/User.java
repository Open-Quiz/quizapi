package net.pumbas.quizapi.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.pumbas.quizapi.user.providers.UserDataProvider.Provider;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "users",  // "user" is a reserved keyword in Postgres
    uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "providerId"})
)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String pictureUrl;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Provider provider;

  @Column(nullable = false)
  private String providerId;

}
