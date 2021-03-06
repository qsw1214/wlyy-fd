package com.yihu.wlyy.controllers.patient.device;

import com.yihu.wlyy.controllers.BaseController;
import com.yihu.wlyy.models.device.PatientDevice;
import com.yihu.wlyy.services.device.DeviceService;
import com.yihu.wlyy.services.device.PatientDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 患者端：设备管理控制类
 * @author George
 *
 */
@Controller
@RequestMapping(value = "patient/device")
@Api(value = "患者设备管理", description = "患者设备管理")
public class PatientDeviceController extends BaseController {

	@Autowired
	private PatientDeviceService patientDeviceService;
	@Autowired
	private DeviceService deviceService;

	@ApiOperation("设备保存接口")
	@RequestMapping(value = "savePatientDevice", produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody
	public String savePatientDevice(@ApiParam(name="json",value="设备数据json")
							  @RequestParam(value="json",required = true) String json) {
		try {
			PatientDevice device = objectMapper.readValue(json,PatientDevice.class);
			// 设置患者标识
			String user = getUID();
//			String user = "CS20160830001";
			device.setUser(user);

			if (!patientDeviceService.saveDevice(device)) {
				return error(-1, "患者绑定设备失败！");
			}
			if (!deviceService.saveDeviceData(device)) {
				return error(-1, "保存设备信息失败！");
			}
			return success("设备保存成功！");
		}
		catch (Exception ex) {
			return invalidUserException(ex, -1, ex.getMessage());
		}
	}

	/**
	 *  设备列表获取
	 * @return 操作结果
	 */
	@ApiOperation("患者设备列表获取")
	@RequestMapping(value = "patientDeviceList", produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody
	public String getDeviceByPatient(@ApiParam(name="id",value="分页起始id",defaultValue = "0")
									  @RequestParam(value="id",required = true) long id,
									 @ApiParam(name="pagesize",value="每页条数",defaultValue = "10")
									 @RequestParam(value="pagesize",required = true) int pagesize) {
		try {
			String user = getUID();
//			String user = "CS20160830001";
			Page<PatientDevice> list = patientDeviceService.findByPatient(user, id, pagesize);
			return write(200, "查询成功", "list", list);
		} catch (Exception ex) {
			return invalidUserException(ex, -1, ex.getMessage());
		}
	}


	@ApiOperation("获取患者设备信息")
	@RequestMapping(value = "patientDeviceInfo", produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody
	public String getPatientDeviceInfo(@ApiParam(name="id",value="患者设备ID",defaultValue = "146")
									 @RequestParam(value="id",required = true) String id) {
		try {
			PatientDevice device = patientDeviceService.findById(id);
			return write(200, "查询成功", "data", device);
		} catch (Exception ex) {
			return invalidUserException(ex, -1, ex.getMessage());
		}
	}

	/**
	 *  通过sn码获取设备绑定情况
	 */
	@ApiOperation("通过sn码获取设备绑定情况")
	@RequestMapping(value = "patientDeviceIdCard", produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody
	public String getDeviceUser(
			@ApiParam(name="openId",value="openId")
			@RequestParam(value="openId",required = true) String openId,
			@ApiParam(name="type",value="设备类型",defaultValue = "1")
									     @RequestParam(value="type",required = true) String type,
										 @ApiParam(name="device_sn",value="设备SN码",defaultValue = "15L000002")
										 @RequestParam(value="device_sn",required = true) String deviceSn) {
		try {
			String user = getUID();
//			String user = "CS20160830001";
			List<Map<String,String>> list = patientDeviceService.getDeviceUser(openId, user,deviceSn,type);
			return write(200, "获取设备绑定信息成功！", "data",list);
		} catch (Exception ex) {
			return invalidUserException(ex, -1, ex.getMessage());
		}
	}

	/**
	 * 设备删除
	 */
	@ApiOperation("设备删除")
	@RequestMapping(value = "deletePatientDevice", produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
	@ResponseBody
	public String delete(@ApiParam(name="id",value="删除设备关联ID")
						  @RequestParam(value="id",required = true) String id) {
		try {
			PatientDevice pd = patientDeviceService.findById(id);
			if (pd!=null) {
				String user = getUID();
//				String user = "CS20160830001";
				if (!StringUtils.equals(pd.getUser(), user)) {
					return error(-1, "只允许删除自己的设备！");
				}
				// 删除设备
				patientDeviceService.deleteDevice(id);
				return success("设备已删除！");
			} else{
				return error(-1, "不存在该设备！");
			}
		} catch (Exception ex) {
			return invalidUserException(ex, -1,ex.getMessage());
		}
	}
}
