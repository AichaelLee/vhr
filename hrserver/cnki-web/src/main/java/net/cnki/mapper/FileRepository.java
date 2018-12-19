package net.cnki.mapper;

import net.cnki.bean.FastDfs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface FileRepository extends JpaRepository<FastDfs, String>{
	
}
