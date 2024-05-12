package com.project.shopapp.untils;

public class MessageKeys {
    public static String LOGIN_SUCCESSFULLY = "user.login.login_successfully";
    public static String LOGIN_FAILED = "user.login.login_failed";

    public static String REGISTER_SUCCESSFULLY = "user.register.register_successfully";
    public static String PASSWORD_NOT_MATCH = "user.register.error.password_not_match";
    public static String REGISTERED_NUMBER_PHONE = "user.register.error.registered_phone_number";
    public static String ROLE_NOT_FOUND = "user.register.error.role_not_found";
    public static String NOT_REGISTER_ADMIN_ACCOUNT = "user.register.error.not_register_admin_account";
    public static String NOT_FOUND_USER_BY_ID = "user.get.error.not_found_user_by_id";
    public static String UPDATE_USER_SUCCESSFULLY = "user.update.update_successfully";
    public static String OLD_PASSWORD_IS_INCORRECT = "user.update.error.old_password_is_incorrect";
    public static String DELETE_USER_SUCCESSFULLY = "user.delete.delete_successfully";

    public static String CREATE_CATEGORY_SUCCESSFULLY = "category.create.create_successfully";
    public static String UPDATE_CATEGORY_SUCCESSFULLY = "category.update.update_successfully";
    public static String DELETE_CATEGORY_SUCCESSFULLY = "category.delete.delete_successfully";
    public static String NOT_FOUND_CATEGORY = "category.get.error.not_found_category";

    public static String DELETE_ORDER_SUCCESSFULLY = "order.delete.delete_successfully";
    public static String NOT_FOUND_ORDER = "order.get.error.not_found_order";
    public static String ORDER_HAS_BEEN_DELETED = "order.get.error.order_has_been_deleted";

    public static String UPDATE_PRODUCT_SUCCESSFULLY = "product.update.update_successfully";
    public static String DELETE_PRODUCT_SUCCESSFULLY = "product.delete.delete_successfully";
    public static String UPDATE_PRODUCT_FAILED = "product.update.error.update_failed";
    public static String ONLY_UPLOAD_FIVE_IMAGE = "product.uploadImage.error.only_upload_five_image";
    public static String SIZE_IS_LESS_BE_THAN_TEN = "product.uploadImage.error.size_less_than_ten";
    public static String FILE_MUST_BE_IMAGE = "product.uploadImage.error.file_must_be_image";
    public static String NO_MORE_FIVE_IMAGE_FOR_PRODUCT = "product.uploadImage.error.no_more_than_5_image";
    public static String NOT_FOUND_PRODUCT = "product.get.error.not.found.product";

    public static String DELETE_ORDER_DETAIL_SUCCESSFULLY = "orderDetail.delete.delete_successfully";
    public static String NOT_FOUND_ORDER_DETAIL = "orderDetail.get.error.not_found_orderDetail";
}
