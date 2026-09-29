
 package devpilot.backend.services;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import devpilot.backend.entity.User;
import devpilot.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserService {
    public final UserRepository userRepository;
   public final  TextEncryptor textEncryptor;


   
   public User upsertFromGitHub(Map<String,Object> attributes, String acessToken, String scope) {
       Long githubId = toLong(attributes.get("id"));
       String login=String.valueOf(attributes.get("login"));
       String name =attributes.get("name")!=null ? String.valueOf(attributes.get("name"))
       :login;
       String avatarUrl = attributes.get("avatar_url") != null
       ? String.valueOf(attributes.get("avatar_url")):null;

       String encryptorToken = textEncryptor.encrypt(acessToken);

       User user = userRepository.findByGithubId(githubId).orElseGet(User::new);
       user.setAccessToken(encryptorToken);
       user.setAvatarUrl(avatarUrl);
       user.setGithubId(githubId);
       user.setGithubUsername(login);
       user.setDisplayName(name);
       user.setTokenScope(scope);

      return userRepository.save(user);

    }
   @Transactional(readOnly = true)
   public User requiredById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
     @Transactional(readOnly = true)
    public String decryptAccessToken(User user) {
        return textEncryptor.decrypt(user.getAccessToken());
    }
    
    private static Long toLong(Object obj){
        if (obj instanceof Number number) {
            return number.longValue();
        } 
        return Long.parseLong(String.valueOf(obj));
    }
    
}