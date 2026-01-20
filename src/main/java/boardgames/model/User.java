            package boardgames.model;

            import ru.kpfu.itis.boardgames.model.enums.UserRole;

            import java.time.LocalDateTime;


            public class User {
                private Long id;
                private String name;
                private Integer age;
                private String username;
                private String password;
                private String email;
                private UserRole role;
                private LocalDateTime createdAt;
                private String profileImage;

                public User() {}

                public User(Long id, String name, Integer age, String username, String password, String email, UserRole role, LocalDateTime createdAt, String profileImage) {
                    this.id = id;
                    this.name = name;
                    this.age = age;
                    this.username = username;
                    this.password = password;
                    this.email = email;
                    this.role = role;
                    this.createdAt = createdAt;
                    this.profileImage = profileImage;
                }

                public User(String name, int age) {
                    this.name = name;
                    this.age = age;
                }
                public  User(String name, int age, String email) {
                    this.name = name;
                    this.age = age;
                    this.email = email;
                }
                public User(String username, String name, Integer age, String email, String password) {
                    this.username = username;
                    this.name = name;
                    this.age = age;
                    this.email = email;
                    this.password = password;
                    this.role = UserRole.USER;
                }

                public Long getId() {
                    return id;
                }

                public void setId(Long id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Integer getAge() {
                    return age;
                }

                public void setAge(Integer age) {
                    this.age = age;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public String getPassword() {
                    return password;
                }

                public void setPassword(String password) {
                    this.password = password;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public UserRole getRole() {
                    return role;
                }

                public void setRole(UserRole role) {
                    this.role = role;
                }

                public LocalDateTime getCreatedAt() {
                    return createdAt;
                }

                public void setCreatedAt(LocalDateTime createdAt) {
                    this.createdAt = createdAt;
                }

                public String getProfileImage() {
                    return profileImage;
                }

                public void setProfileImage(String profileImage) {
                    this.profileImage = profileImage;
                }
            }
