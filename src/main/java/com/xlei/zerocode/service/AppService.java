package com.xlei.zerocode.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.xlei.zerocode.common.DeleteRequest;
import com.xlei.zerocode.model.dto.app.AppAddRequest;
import com.xlei.zerocode.model.dto.app.AppAdminUpdateRequest;
import com.xlei.zerocode.model.dto.app.AppQueryRequest;
import com.xlei.zerocode.model.dto.app.AppUpdateRequest;
import com.xlei.zerocode.model.entity.App;
import com.xlei.zerocode.model.vo.AppVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author xlei
 */
public interface AppService extends IService<App> {

    Long addApp(AppAddRequest appAddRequest, HttpServletRequest request);

    Boolean updateApp(AppUpdateRequest updateRequest, HttpServletRequest request);

    Boolean deleteApp(DeleteRequest deleteRequest, HttpServletRequest request);

    AppVO getAppVO(App app);

    QueryWrapper getQueryWrapper(AppQueryRequest request);

    List<AppVO> getAppVOList(List<App> appList);

    Page<AppVO> userAppVOByPage(AppQueryRequest appQueryRequest, HttpServletRequest request);

    Boolean adminDeleteApp(DeleteRequest deleteRequest, HttpServletRequest request);

    Boolean adminUpdateApp(AppAdminUpdateRequest updateRequest);
}
