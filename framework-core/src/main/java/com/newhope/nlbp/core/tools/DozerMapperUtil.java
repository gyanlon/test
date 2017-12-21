package com.newhope.nlbp.core.tools;


import org.dozer.DozerBeanMapper;

/**
 * Dozer配置文件加载
 * 
 * @author sy
 * 
 */
public class DozerMapperUtil {

    private static DozerBeanMapper mapper = new DozerBeanMapper();

    /*
     * static { List<String> myMappingFiles = new ArrayList<String>();
     * myMappingFiles
     * .add("com/crp/cont/dto/Cux5PoContracthAllListDTOMapper.xml");
     * mapper.setMappingFiles(myMappingFiles);// 加载映射文件 }
     */
    public static DozerBeanMapper getMapper() {
        return mapper;
    }

}