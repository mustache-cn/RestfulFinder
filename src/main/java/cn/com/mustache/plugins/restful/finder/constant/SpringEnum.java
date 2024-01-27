package cn.com.mustache.plugins.restful.finder.constant;

import java.util.Arrays;

/**
 * @author steven
 */
public interface SpringEnum {

    enum Rest {

        /**
         * POST
         */
        REQUEST_MAPPING("RequestMapping", HttpMethodEnum.UNKNOWN, "org.springframework.web.bind.annotation.RequestMapping"),
        GET_MAPPING("GetMapping", HttpMethodEnum.GET, "org.springframework.web.bind.annotation.GetMapping"),
        POST_MAPPING("PostMapping", HttpMethodEnum.POST, "org.springframework.web.bind.annotation.PostMapping"),
        PUT_MAPPING("PutMapping", HttpMethodEnum.PUT, "org.springframework.web.bind.annotation.PutMapping"),
        DELETE_MAPPING("DeleteMapping", HttpMethodEnum.DELETE, "org.springframework.web.bind.annotation.DeleteMapping"),
        PATCH_MAPPING("PatchMapping", HttpMethodEnum.PATCH, "org.springframework.web.bind.annotation.PatchMapping");

        private final String name;
        private final HttpMethodEnum method;

        private final String annotation;

        Rest(String name, HttpMethodEnum method, String annotation) {
            this.name = name;
            this.method = method;
            this.annotation = annotation;
        }

        public static Rest getByAnnotation(String annotation) {
            return Arrays.stream(values()).filter(an -> an.getAnnotation().equals(annotation)).findFirst().orElse(null);
        }

        public static Rest getByName(String annotation) {
            return Arrays.stream(values()).filter(an -> an.getName().equals(annotation)).findFirst().orElse(null);
        }

        public static boolean existsByAnnotation(String annotation) {
            return Arrays.stream(values()).anyMatch(an -> an.getAnnotation().equals(annotation));
        }

        public static boolean existsByName(String annotation) {
            return Arrays.stream(values()).anyMatch(an -> an.getName().equals(annotation));
        }

        public String getName() {
            return name;
        }

        public HttpMethodEnum getMethod() {
            return method;
        }

        public String getAnnotation() {
            return annotation;
        }
    }

    enum Controller {

        /**
         * Controller
         */
        CONTROLLER("Controller", "org.springframework.stereotype.Controller"),
        /**
         * RestController
         */
        REST_CONTROLLER("RestController", "org.springframework.web.bind.annotation.RestController");

        Controller(String shortName, String qualifiedName) {
            this.shortName = shortName;
            this.qualifiedName = qualifiedName;
        }

        private final String shortName;
        private final String qualifiedName;

        public String getQualifiedName() {
            return qualifiedName;
        }

        public String getShortName() {
            return shortName;
        }

    }

}
