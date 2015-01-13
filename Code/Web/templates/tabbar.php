<?php

class Tab {
	private $label;
	private $attrs;
	private $href;
	public function __construct($label,$attrs=[],$href=null){
		if($href === null){
			$this->href = '#'.str_replace(' ','',$label);
		} else {
			$this->href = $href;
		}
		$this->label = $label;
		$this->attrs = $attrs;
	}
	public function __toString(){
		$ret = '<dd';
		foreach($this->attrs as $key => $value){
			$ret.=' '.$key.'="'.$value.'"';
		}
		return $ret.'><a href="'.$this->href.'">'.$this->label.'</a></dd>';
	}
}

function make_tabbar($tabs){
	echo '<dl class="tabs vertical" data-tab>';
	for($i = 0; $i < count($tabs); ++$i){
		echo $tabs[$i];
	}
	echo '</dl>';
}

?>
