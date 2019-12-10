package pl.koszela.spring.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.koszela.spring.crudFiles.CreateFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Controller
public class UploadController {

    private CreateFile createFile;

    public UploadController(CreateFile createFile) {
        this.createFile = Objects.requireNonNull(createFile);
    }

    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        String name = "";
        Cookie[] cookies = request.getCookies();
        model.addAttribute("cookie", cookies[1].getValue());
        model.addAttribute("nameFolder", name);
        return "upload";
    }

    @PostMapping("/upload")
    public String fileUpload(@RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes, @RequestParam String nameFolder, HttpServletResponse response) {
        createFile.createFile(file, redirectAttributes, nameFolder, response);
        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus(@ModelAttribute("message") String message, Model model, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Cookie[] cookies = request.getCookies();
        model.addAttribute("message", message);
        Cookie cookie = new Cookie("URI", request.getRequestURI());
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        for (Cookie cookie1 : cookies) {
            if (cookie1.getName().equalsIgnoreCase("url")) {
                model.addAttribute("cookie", cookie1.getValue());
            }
        }
        return "uploadStatus";
    }
}