using System;
using System.Collections.Generic;

namespace HumanResourceServer.Models
{
    public partial class Departments
    {
        public Departments()
        {
            Workers = new HashSet<Workers>();
        }

        public int Id { get; set; }
        public string NameOfDepartment { get; set; }

        public ICollection<Workers> Workers { get; set; }
    }
}
