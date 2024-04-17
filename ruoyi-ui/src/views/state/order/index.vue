<template>
  <div class="app-container">
    <el-checkbox :indeterminate="checkPoint" v-model="checkAll" @change="checkAllChange" />
    <el-checkbox-group v-model="checkList" @change="checkedChange">
      <el-checkbox v-for="item in allList" :label="item.id" :key="item.id">
        <el-image :src="item.url" fit="cover" style="width: 100px; height: 100px;"/>
      </el-checkbox>
    </el-checkbox-group>
    <el-row>
      <el-col :span="2" v-for="item in allList" :key="item.id" offset="2">
        <el-card >
          <el-image :src="item.url" fit="cover" style="width: 100px; height: 100px;display: block"/>
          <el-input :value="item.name" />
          <el-checkbox v-model="item.id" :label="item.id" style="float: right"/>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data(){
    return {
      allList: [],
      checkPoint: true,
      checkAll: false,
      checkList: [],
      imgUrl: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg"
    }
  },
  methods: {
    getCheckList(){
      this.allList = [
        {"id": "id1", "url": "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg", "name": "name1"},
        {"id": "id2", "url": "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg", "name": "name2"},
        {"id": "id3", "url": "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg", "name": "name3"}
      ];
    },
    checkAllChange(val){
      console.log(val)
      this.checkList = val ? this.allList.map(item => item.id) : [];
      this.checkPoint = false;
    },
    checkedChange(val){
      this.checkAll = val.length === this.allList.length;
      this.checkPoint = val.length > 0 && val.length < this.allList.length;
    }
  },
  created() {
    this.getCheckList();
  }
}
</script>
