package com.gaea.single.bridge.dto.user;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@ApiModel("设置相册封面")
@Getter
@Setter
public class SetAlbumCoverReq extends DeleteAlbumReq {}
